package team.redrock.stickerset.main.model.database.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.Parcelize
import team.redrock.stickerset.main.model.data.StickerSet
import java.io.Serializable

@Entity(tableName = "sticker_set")
data class StickerSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val title: String,
    val imgPath: String?,
): Serializable

data class StickerSetQueryResult(
    @Embedded
    val info: StickerSet,
    @Relation(parentColumn = "id", entityColumn = "pid")
    val stickers: List<StickerEntity>
)