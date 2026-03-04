package team.bue.bugle.core.network.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponseDto(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
)
