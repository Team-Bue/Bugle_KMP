package team.bue.bugle.core.network.service

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import team.bue.bugle.core.network.dto.mail.SendMailCodeRequestDto
import team.bue.bugle.core.network.dto.mail.VerifyMailCodeRequestDto
import team.bue.bugle.core.network.dto.mail.VerifyMailCodeResponseDto

class MailApiService(
    private val client: HttpClient,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun sendCode(request: SendMailCodeRequestDto) {
        client.post("/mails/send") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }

    suspend fun verifyCode(request: VerifyMailCodeRequestDto): VerifyMailCodeResponseDto {
        val response =
            client.post("/mails/verify") {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(request)
            }

        val rawBody = response.bodyAsText().trim()
        if (rawBody.isBlank()) {
            return VerifyMailCodeResponseDto(token = null)
        }

        return runCatching {
            json.decodeFromString(VerifyMailCodeResponseDto.serializer(), rawBody)
        }.getOrElse {
            VerifyMailCodeResponseDto(token = rawBody.removeSurrounding("\""))
        }
    }
}
