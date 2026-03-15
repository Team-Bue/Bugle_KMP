package team.bue.bugle.core.network.dto.like

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LikedPostItemResponseDto(
    val id: Long,
    @SerialName("object_key")
    val objectKey: String,
)

@Serializable
data class LikedPostListResponseDto(
    val posts: List<LikedPostItemResponseDto>,
)
