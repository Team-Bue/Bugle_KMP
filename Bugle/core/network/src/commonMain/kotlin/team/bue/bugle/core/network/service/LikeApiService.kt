package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import team.bue.bugle.core.network.dto.like.LikedPostListResponseDto

class LikeApiService(
    private val client: HttpClient,
) {
    suspend fun likePost(postId: Long) {
        client.post("/likes/$postId")
    }

    suspend fun unlikePost(postId: Long) {
        client.delete("/likes/$postId")
    }

    suspend fun getLikedPosts(page: Int? = null): LikedPostListResponseDto =
        client.get("/likes/posts") {
            page?.let { parameter("page", it) }
        }.body()
}
