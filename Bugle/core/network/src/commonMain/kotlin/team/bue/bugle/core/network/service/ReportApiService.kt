package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import team.bue.bugle.core.network.dto.report.CreateReportRequestDto

class ReportApiService(
    private val client: HttpClient,
) {
    suspend fun report(
        reportedId: Long,
        request: CreateReportRequestDto,
    ) {
        client.post("/reports/$reportedId") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }
}
