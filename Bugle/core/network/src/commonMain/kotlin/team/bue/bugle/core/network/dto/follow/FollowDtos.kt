package team.bue.bugle.core.network.dto.follow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowUserResponseDto(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
)

@Serializable
data class FollowUserListResponseDto(
    val users: List<FollowUserResponseDto>,
)
