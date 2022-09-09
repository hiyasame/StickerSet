package team.redrock.stickerset.main.model.repository

import android.util.Log
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import team.redrock.rain.lib.common.BaseApp
import team.redrock.rain.lib.common.extensions.SP_TOKEN
import team.redrock.rain.lib.common.extensions.defaultSp
import team.redrock.stickerset.main.model.data.StickerData
import team.redrock.stickerset.main.model.data.StickerSet
import team.redrock.stickerset.main.model.data.StickerSetData
import team.redrock.stickerset.main.model.database.AppDatabase
import team.redrock.stickerset.main.model.database.entity.StickerEntity
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.model.network.ApiService
import java.nio.file.Files
import java.nio.file.Paths

object TelegramRepository {

    var token: String?
        get() = defaultSp.getString(SP_TOKEN, null)
        set(value) {
            defaultSp.edit {
                putString(SP_TOKEN, value)
            }
        }

    private val stickerDir by lazy {
        BaseApp.appContext.filesDir.resolve("stickers")
    }

    suspend fun deleteStickerSet(entity: StickerSetEntity) {
        fetchStickers(entity.id!!).forEach {
            Files.delete(Paths.get(it.imgPath))
        }
        AppDatabase.INSTANCE.stickerSetDao()
            .deleteStickerSet(entity)
    }

    /**
     * 从网络上拉取数据并缓存到数据库
     *
     * @return
     */
    suspend fun getStickerSet(name: String, notifyFlow: MutableSharedFlow<Pair<Int, Int>>): Result<StickerSetData> {
        if (token == null) {
            return Result.failure(RuntimeException("未设置Token"))
        }
        return ApiService.getStickerSet(token!!, name)
            .warp()
            .map { mapStickerSet(it, notifyFlow) }
            .onSuccess { AppDatabase.INSTANCE.stickerSetDao().addStickerSet(it) }
    }

    /**
     * 从数据库中拉取StickerSet数据
     *
     * @return
     */
    fun fetchStickerSets(): Flow<List<StickerSetEntity>> {
        return AppDatabase.INSTANCE.stickerSetDao().fetchStickerSets()
    }

    /**
     * 从数据库中拉取Sticker数据
     *
     * @param parentId StickerSet id
     * @return
     */
    suspend fun fetchStickers(parentId: Int): List<StickerEntity> {
        return AppDatabase.INSTANCE.stickerSetDao().fetchStickersByPid(parentId)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun mapStickerSet(set: StickerSet, notifyFlow: MutableSharedFlow<Pair<Int, Int>>): StickerSetData = coroutineScope {
        if (!stickerDir.exists()) {
            stickerDir.mkdirs()
        }
        var count = 0
        val mutex = Mutex()
        // 多协程并发下载, 速度比串行下载高了不少
        // 接下来可以实现单个文件级别的多协程并发下载
        val stickers = set.stickers.map {
                async(Dispatchers.IO) {
                    val teleFile = ApiService.getFile(token!!, it.fileId).warp().getOrNull()
                        ?: return@async null
                    val path = teleFile.filePath
                    val fileBytes = ApiService.getWebFile(teleFile.fileUrl(token!!))
                    val file = stickerDir.resolve(path)
                    if (!file.exists()) {
                        file.parentFile?.mkdirs()
                        file.createNewFile()
                    }
                    file.writeBytes(fileBytes)
                    // 防止产生并发问题，上个锁
                    mutex.lock()
                    notifyFlow.emit(++count to set.stickers.size)
                    mutex.unlock()
                    StickerData(
                        fileUniqueId = it.fileUniqueId,
                        imgPath = file.path
                    )
                }
            }.mapNotNull { it.await() }
        val imgPath = set.thumb?.fileId?.let {
            val teleFile = ApiService.getFile(token!!, it).warp().getOrNull() ?: return@let null
            val path = teleFile.filePath
            val fileBytes = ApiService.getWebFile(teleFile.fileUrl(token!!))
            val file = stickerDir.resolve(path)
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            file.writeBytes(fileBytes)
            file.path
        } ?: stickers[0].imgPath
        return@coroutineScope StickerSetData(set.name, set.title, imgPath, stickers)
    }
}