package team.redrock.stickerset.main.model.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sticker")
data class StickerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val pid: Int,
    val uniqueFileId: String,
    val imgPath: String
)