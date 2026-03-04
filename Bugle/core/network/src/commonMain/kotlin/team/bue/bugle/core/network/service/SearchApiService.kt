package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import team.bue.bugle.core.network.dto.post.TotalPageCountResponseDto
import team.bue.bugle.core.network.dto.search.SearchPostListResponseDto
import team.bue.bugle.core.network.dto.search.SearchPostTypeDto
import team.bue.bugle.core.network.dto.search.SearchUserListResponseDto

class SearchApiService(
    private val client: HttpClient,
) {
    suspend fun searchPosts(
        type: SearchPostTypeDto,
        keyword: String,
        page: Int? = null,
    ): SearchPostListResponseDto =
        client.get("/search/posts") {
            parameter("type", type.name)
            parameter("keyword", keyword)
            page?.let { parameter("page", it) }
        }.body()

    suspend fun getSearchPostsCount(
        type: SearchPostTypeDto,
        keyword: String,
    ): TotalPageCountResponseDto =
        client.get("/search/posts/count") {
            parameter("type", type.name)
            parameter("keyword", keyword)
        }.body()

    suspend fun searchUsers(
        keyword: String,
        page: Int? = null,
    ): SearchUserListResponseDto =
        client.get("/search/users") {
            parameter("keyword", keyword)
            page?.let { parameter("page", it) }
        }.body()

    suspend fun getSearchUsersCount(keyword: String): TotalPageCountResponseDto =
        client.get("/search/users/count") {
            parameter("keyword", keyword)
        }.body()
}
