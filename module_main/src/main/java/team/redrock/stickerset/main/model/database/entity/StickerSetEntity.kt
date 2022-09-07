package team.redrock.stickerset.main.model.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import team.redrock.stickerset.main.model.data.StickerSet

@Entity(tableName = "sticker_set")
data class StickerSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val title: String,
    val imgPath: String?,
)

data class StickerSetQueryResult(
    @Embedded
    val info: StickerSet,
    @Relation(parentColumn = "id", entityColumn = "pid")
    val stickers: List<StickerEntity>
)