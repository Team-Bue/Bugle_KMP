package team.bue.bugle.core.network.dto.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IssuePresignedUrlRequestDto(
    @SerialName("file_type")
    val fileType: FileTypeDto,
    @SerialName("file_name")
    val fileName: String,
)

@Serializable
enum class FileTypeDto {
    @SerialName("IMAGE_FILE")
    IMAGE_FILE,

    @SerialName("VIDEO_FILE")
    VIDEO_FILE,
}

@Serializable
data class IssuePresignedUrlResponseDto(
    @SerialName("object_key")
    val objectKey: String,
    @SerialName("presigned_url")
    val presignedUrl: String,
)
