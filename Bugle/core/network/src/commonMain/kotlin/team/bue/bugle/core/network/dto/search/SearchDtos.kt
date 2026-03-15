package team.bue.bugle.core.network.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SearchPostTypeDto {
    @SerialName("CONTENT")
    CONTENT,

    @SerialName("LOCATION")
    LOCATION,
}

@Serializable
data class SearchPostItemResponseDto(
    val id: Long,
    @SerialName("object_key")
    val objectKey: String,
)

@Serializable
data class SearchPostListResponseDto(
    val posts: List<SearchPostItemResponseDto>,
)

@Serializable
data class SearchUserItemResponseDto(
    val id: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
)

@Serializable
data class SearchUserListResponseDto(
    val users: List<SearchUserItemResponseDto>,
)
