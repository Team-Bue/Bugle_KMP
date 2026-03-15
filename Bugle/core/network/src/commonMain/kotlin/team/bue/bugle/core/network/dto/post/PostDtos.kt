package team.bue.bugle.core.network.dto.post

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpsertPostRequestDto(
    val content: String,
    val country: String? = null,
    val region: String? = null,
    @SerialName("object_key")
    val objectKey: String,
)

@Serializable
data class PostSummaryResponseDto(
    val id: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
    val country: String? = null,
    val region: String? = null,
    @SerialName("object_key")
    val objectKey: String? = null,
    val content: String,
)

@Serializable
data class PostListResponseDto(
    val posts: List<PostSummaryResponseDto>,
)

@Serializable
data class PostDetailResponseDto(
    val id: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
    val country: String? = null,
    val region: String? = null,
    @SerialName("object_key")
    val objectKey: String? = null,
    val content: String,
)

@Serializable
data class TotalPageCountResponseDto(
    @SerialName("total_page_count")
    val totalPageCount: Int,
)
