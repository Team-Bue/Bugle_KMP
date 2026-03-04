package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import team.bue.bugle.core.network.dto.follow.FollowUserListResponseDto
import team.bue.bugle.core.network.dto.post.TotalPageCountResponseDto

class FollowApiService(
    private val client: HttpClient,
) {
    suspend fun follow(userId: Long) {
        client.post("/follows/$userId")
    }

    suspend fun unfollow(userId: Long) {
        client.delete("/follows/$userId")
    }

    suspend fun getFollowers(page: Int? = null): FollowUserListResponseDto =
        client.get("/follows/follower") {
            page?.let { parameter("page", it) }
        }.body()

    suspend fun getFollowersCount(): TotalPageCountResponseDto = client.get("/follows/follower/count").body()

    suspend fun getFollowings(page: Int? = null): FollowUserListResponseDto =
        client.get("/follows/following") {
            page?.let { parameter("page", it) }
        }.body()

    suspend fun getFollowingsCount(): TotalPageCountResponseDto = client.get("/follows/following/count").body()
}
