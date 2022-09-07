package team.redrock.stickerset.main.model.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import team.redrock.stickerset.main.model.data.StickerSetData
import team.redrock.stickerset.main.model.database.entity.StickerEntity
import team.redrock.stickerset.main.model.database.entity.StickerSetEntity

@Dao
interface StickerSetDao {
    @Query("SELECT * FROM sticker_set")
    fun fetchStickerSets(): Flow<List<StickerSetEntity>>

    @Query("SELECT * FROM sticker WHERE pid = :pid")
    fun fetchStickersByPid(pid: Int): Flow<List<StickerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStickerSetEntity(entity: StickerSetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStickerEntity(entity: StickerEntity): Long

    @Delete
    suspend fun deleteStickerSetEntity(entity: StickerSetEntity)

    @Query("DELETE FROM sticker WHERE pid = :pid")
    suspend fun deleteStickerEntityByPid(pid: Int)

    @Transaction
    suspend fun deleteStickerSet(entity: StickerSetEntity) {
        deleteStickerSetEntity(entity)
        deleteStickerEntityByPid(entity.id!!)
    }

    @Transaction
    suspend fun addStickerSet(data: StickerSetData) {
        val rowId = addStickerSetEntity(
            StickerSetEntity(
                name = data.name,
                title = data.title,
                imgPath = data.imgPath
            )
        ).toInt()
        data.stickers.forEach { sticker ->
            addStickerEntity(
                StickerEntity(
                    pid = rowId,
                    uniqueFileId = sticker.fileUniqueId,
                    imgPath = sticker.imgPath
                )
            )
        }
    }
}