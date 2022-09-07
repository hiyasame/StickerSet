package team.redrock.stickerset.main.model.repository

import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow
import team.redrock.rain.lib.common.config.BaseApp
import team.redrock.rain.lib.common.config.extensions.SP_TOKEN
import team.redrock.rain.lib.common.config.extensions.defaultSp
import team.redrock.stickerset.main.model.data.StickerData
import team.redrock.stickerset.main.model.data.StickerSet
import team.redrock.stickerset.main.model.data.StickerSetData
import team.redrock.stickerset.main.model.database.AppDatabase
import team.redrock.stickerset.main.model.database.entity.StickerEntity
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity
import team.redrock.stickerset.main.model.network.ApiService

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

    /**
     * 从网络上拉取数据并缓存到数据库
     *
     * @return
     */
    suspend fun getStickerSet(name: String): Result<StickerSetData> {
        if (token == null) {
            return Result.failure(RuntimeException("未设置Token"))
        }
        return mapStickerSet(ApiService.getStickerSet(token!!, name))
            .also { AppDatabase.INSTANCE.stickerSetDao().addStickerSet(it) }
            .let { Result.success(it) }
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
    fun fetchStickers(parentId: Int): Flow<List<StickerEntity>> {
        return AppDatabase.INSTANCE.stickerSetDao().fetchStickersByPid(parentId)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun mapStickerSet(set: StickerSet): StickerSetData {
        if (!stickerDir.exists()) {
            stickerDir.mkdirs()
        }
        val stickers = set.stickers.map {
            val teleFile = ApiService.getFile(token!!, it.fileId)
            val path = teleFile.filePath
            val fileBytes = ApiService.getWebFile(teleFile.fileUrl(token!!))
            val file = stickerDir.resolve(path)
            if (!file.exists()) {
                file.createNewFile()
            }
            file.writeBytes(fileBytes)
            StickerData(
                fileUniqueId = it.fileUniqueId,
                imgPath = file.path
            )
        }
        val imgPath = set.thumb?.fileId?.let {
            val teleFile = ApiService.getFile(token!!, it)
            val path = teleFile.filePath
            val fileBytes = ApiService.getWebFile(teleFile.fileUrl(token!!))
            val file = stickerDir.resolve(path)
            file.createNewFile()
            file.writeBytes(fileBytes)
            path
        }
        return StickerSetData(set.name, set.title, imgPath, stickers)
    }
}