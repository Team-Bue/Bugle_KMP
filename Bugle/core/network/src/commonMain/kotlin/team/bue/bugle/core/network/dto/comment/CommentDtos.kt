package team.bue.bugle.core.network.dto.comment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpsertCommentRequestDto(
    val content: String,
)

@Serializable
data class CommentUserResponseDto(
    val id: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
)

@Serializable
data class CommentResponseDto(
    val id: Long,
    val content: String,
    val user: CommentUserResponseDto,
)

@Serializable
data class CommentListResponseDto(
    val comments: List<CommentResponseDto>,
)
