package team.bue.bugle.core.data.datasource

import team.bue.bugle.core.model.auth.FindAccountIdRequest
import team.bue.bugle.core.model.auth.FindAccountIdResult
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

    suspend fun findAccountId(request: FindAccountIdRequest): FindAccountIdResult

    suspend fun resetPassword(request: ResetPasswordRequest)
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

    override suspend fun findAccountId(request: FindAccountIdRequest): FindAccountIdResult {
        return authApiService.findAccountId(request.toDto()).toDomain()
    }

    override suspend fun resetPassword(request: ResetPasswordRequest) {
        authApiService.resetPassword(request.toDto())
    }
}
