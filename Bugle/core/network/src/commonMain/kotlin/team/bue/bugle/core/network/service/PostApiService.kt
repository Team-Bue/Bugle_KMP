package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import team.bue.bugle.core.network.dto.post.PostDetailResponseDto
import team.bue.bugle.core.network.dto.post.PostListResponseDto
import team.bue.bugle.core.network.dto.post.TotalPageCountResponseDto
import team.bue.bugle.core.network.dto.post.UpsertPostRequestDto

class PostApiService(
    private val client: HttpClient,
) {
    suspend fun createPost(request: UpsertPostRequestDto) {
        client.post("/posts") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }

    suspend fun getPosts(page: Int? = null): PostListResponseDto =
        client.get("/posts") {
            page?.let { parameter("page", it) }
        }.body()

    suspend fun getPostsCount(): TotalPageCountResponseDto = client.get("/posts/count").body()

    suspend fun updatePost(
        postId: Long,
        request: UpsertPostRequestDto,
    ) {
        client.patch("/posts/$postId") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }

    suspend fun deletePost(postId: Long) {
        client.delete("/posts/$postId")
    }

    suspend fun getPost(postId: Long): PostDetailResponseDto = client.get("/posts/$postId").body()
}
