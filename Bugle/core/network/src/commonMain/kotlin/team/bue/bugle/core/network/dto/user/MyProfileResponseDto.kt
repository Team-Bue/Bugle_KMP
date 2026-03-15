package team.bue.bugle.core.network.dto.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import team.bue.bugle.core.model.user.MyProfile

@Serializable
data class MyProfileResponseDto(
    @SerialName("user_id")
    val userId: Long,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("profile_image_object_key")
    val profileImageObjectKey: String? = null,
)

fun MyProfileResponseDto.toDomain(): MyProfile =
    MyProfile(
        userId = userId,
        accountId = accountId,
        userName = userName,
        profileImageObjectKey = profileImageObjectKey,
    )
