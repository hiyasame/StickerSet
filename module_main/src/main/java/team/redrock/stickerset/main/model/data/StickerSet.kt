package team.redrock.stickerset.main.model.data

import com.google.gson.annotations.SerializedName as Name

data class StickerSet(
    val name: String,
    val title: String,
    @Name("is_animated") val isAnimated: Boolean,
    @Name("contains_masks") val containsMasks: Boolean,
    @Name("stickers") val stickers: List<Sticker>,
    val thumb: PhotoSize?
)

data class Sticker(
    @Name(FilesFields.fileId) val fileId: String,
    @Name(FilesFields.fileUniqueId) val fileUniqueId: String,
    @Name(FilesFields.width) val width: Int,
    @Name(FilesFields.height) val height: Int,
    @Name(FilesFields.isAnimated) val isAnimated: Boolean,
    @Name(FilesFields.thumb) val thumb: PhotoSize? = null,
    @Name(FilesFields.emoji) val emoji: String?,
    @Name(FilesFields.setName)val setName: String? = null,
    @Name(FilesFields.maskPosition) val maskPosition: MaskPosition? = null,
    @Name(FilesFields.fileSize) val fileSize: Int? = null
)

data class MaskPosition(
    val point: String,
    @Name("x_shift") val xShift: Float,
    @Name("y_shift") val yShift: Float,
    val scale: Float
)

data class PhotoSize(
    @Name(FilesFields.fileId) val fileId: String,
    @Name(FilesFields.fileUniqueId) val fileUniqueId: String,
    @Name(FilesFields.width) val width: Int,
    @Name(FilesFields.height) val height: Int,
    @Name(FilesFields.fileSize) val fileSize: Int? = null
)

internal object FilesFields {
    const val fileId = "file_id"
    const val fileUniqueId = "file_unique_id"
    const val width = "width"
    const val height = "height"
    const val duration = "duration"
    const val thumb = "thumb"
    const val fileName = "file_name"
    const val mimeType = "mime_type"
    const val fileSize = "file_size"
    const val performer = "performer"
    const val title = "title"
    const val length = "length"
    const val isAnimated = "is_animated"
    const val emoji = "emoji"
    const val setName = "set_name"
    const val maskPosition = "mask_position"
    const val filePath = "file_path"
    const val smallFileId = "small_file_id"
    const val smallFileUniqueId = "small_file_unique_id"
    const val bigFileId = "big_file_id"
    const val bigFileUniqueId = "big_file_unique_id"
}