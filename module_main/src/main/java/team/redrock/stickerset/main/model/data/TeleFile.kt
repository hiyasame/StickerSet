package team.redrock.stickerset.main.model.data

import com.google.gson.annotations.SerializedName

data class TeleFile(
    @SerializedName("file_id") val fileId: String,
    @SerializedName("file_unique_id") val fileUniqueId: String,
    @SerializedName("file_size") val fileSize: String,
    @SerializedName("file_path") val filePath: String
) {
    fun fileUrl(token: String): String = "https://api.telegram.org/file/bot$token/$filePath"
}