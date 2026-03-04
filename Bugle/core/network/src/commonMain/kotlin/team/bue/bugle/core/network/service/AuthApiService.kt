package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import team.bue.bugle.core.network.dto.auth.FindAccountIdRequestDto
import team.bue.bugle.core.network.dto.auth.FindAccountIdResponseDto
import team.bue.bugle.core.network.dto.auth.LoginRequestDto
import team.bue.bugle.core.network.dto.auth.ResetPasswordRequestDto
import team.bue.bugle.core.network.dto.auth.SignUpRequestDto
import team.bue.bugle.core.network.dto.auth.TokenPairResponseDto

class AuthApiService(
    private val client: HttpClient,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun login(request: LoginRequestDto): TokenPairResponseDto =
        client.post("/auth/login") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }.body()

    suspend fun signUp(request: SignUpRequestDto): TokenPairResponseDto =
        client.post("/auth/signup") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }.body()

    suspend fun logout(accessToken: String) {
        client.delete("/auth/logout") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }

    suspend fun withdraw(accessToken: String) {
        client.delete("/auth/withdraw") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
    }

    suspend fun reissue(refreshToken: String): TokenPairResponseDto =
        client.post("/auth/reissue") {
            header(HttpHeaders.Authorization, "Bearer $refreshToken")
        }.body()

    suspend fun resetPassword(request: ResetPasswordRequestDto) {
        client.patch("/auth/password") {
            header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody(request)
        }
    }

    suspend fun findAccountId(request: FindAccountIdRequestDto): FindAccountIdResponseDto {
        val response =
            client.post("/auth/account-id") {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(request)
            }

        val rawBody = response.bodyAsText().trim()
        if (rawBody.isBlank()) {
            return FindAccountIdResponseDto(accountId = "")
        }

        return runCatching {
            json.decodeFromString(FindAccountIdResponseDto.serializer(), rawBody)
        }.getOrElse {
            FindAccountIdResponseDto(accountId = rawBody.removeSurrounding("\""))
        }
    }

    suspend fun startKakaoOAuth() {
        client.get("/auth/oauth2/authorization/kakao")
    }

    suspend fun completeKakaoOAuth(): TokenPairResponseDto = client.get("/bugle/oauth2/kakao").body()
}
