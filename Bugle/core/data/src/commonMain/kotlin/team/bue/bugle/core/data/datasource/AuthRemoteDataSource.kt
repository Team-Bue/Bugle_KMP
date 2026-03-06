package team.bue.bugle.core.data.datasource

import team.bue.bugle.core.model.auth.LoginRequest
import team.bue.bugle.core.model.auth.ResetPasswordRequest
import team.bue.bugle.core.model.auth.SignUpRequest
import team.bue.bugle.core.model.auth.TokenPair
import team.bue.bugle.core.network.dto.auth.toDomain
import team.bue.bugle.core.network.dto.auth.toDto
import team.bue.bugle.core.network.service.AuthApiService

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequest): TokenPair

    suspend fun signUp(request: SignUpRequest): TokenPair

    suspend fun resetPassword(request: ResetPasswordRequest)

    suspend fun startKakaoOAuth(): String

    suspend fun completeKakaoOAuth(
        code: String,
        state: String?,
    ): TokenPair
}

class AuthRemoteDataSourceImpl(
    private val authApiService: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun login(request: LoginRequest): TokenPair {
        return authApiService.login(request.toDto()).toDomain()
    }

    override suspend fun signUp(request: SignUpRequest): TokenPair {
        return authApiService.signUp(request.toDto()).toDomain()
    }

    override suspend fun resetPassword(request: ResetPasswordRequest) {
        authApiService.resetPassword(request.toDto())
    }

    override suspend fun startKakaoOAuth(): String = authApiService.startKakaoOAuth()

    override suspend fun completeKakaoOAuth(
        code: String,
        state: String?,
    ): TokenPair =
        authApiService.completeKakaoOAuth(
            code = code,
            state = state,
        ).toDomain()
}
