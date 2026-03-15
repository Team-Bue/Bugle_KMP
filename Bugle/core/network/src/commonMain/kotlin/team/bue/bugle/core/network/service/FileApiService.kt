package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import team.bue.bugle.core.network.dto.file.IssuePresignedUrlRequestDto
import team.bue.bugle.core.network.dto.file.IssuePresignedUrlResponseDto

class FileApiService(
    private val client: HttpClient,
) {
    suspend fun issuePresignedUrl(request: IssuePresignedUrlRequestDto): IssuePresignedUrlResponseDto =
        client.post("/files/pre-signed") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }.body()
}
