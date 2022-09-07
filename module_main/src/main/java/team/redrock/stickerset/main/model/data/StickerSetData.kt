package team.redrock.stickerset.main.model.data

// 传给View层的数据类
data class StickerSetData(
    val name: String,
    val title: String,
    val imgPath: String?,
    val stickers: List<StickerData>
)

data class StickerData(
    val fileUniqueId: String,
    val imgPath: String
)