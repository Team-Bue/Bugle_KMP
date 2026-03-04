package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import team.bue.bugle.core.network.dto.comment.CommentListResponseDto
import team.bue.bugle.core.network.dto.comment.UpsertCommentRequestDto

class CommentApiService(
    private val client: HttpClient,
) {
    suspend fun createComment(
        postId: Long,
        request: UpsertCommentRequestDto,
    ) {
        client.post("/comments/$postId") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }

    suspend fun updateComment(
        commentId: Long,
        request: UpsertCommentRequestDto,
    ) {
        client.patch("/comments/$commentId") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }

    suspend fun deleteComment(commentId: Long) {
        client.delete("/comments/$commentId")
    }

    suspend fun getComments(postId: Long): CommentListResponseDto = client.get("/comments/$postId").body()
}
