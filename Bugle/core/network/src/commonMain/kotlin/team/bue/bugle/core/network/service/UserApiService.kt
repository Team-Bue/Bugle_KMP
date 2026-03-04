package team.bue.bugle.core.network.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import team.bue.bugle.core.network.dto.user.MyProfileResponseDto
import team.bue.bugle.core.network.dto.user.UserProfileResponseDto

class UserApiService(
    private val client: HttpClient,
) {
    suspend fun getMyProfile(accessToken: String): MyProfileResponseDto =
        client.get("/users/me") {
            headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()

    suspend fun getUser(userId: Long): UserProfileResponseDto = client.get("/users/$userId").body()
}
