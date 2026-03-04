package team.bue.bugle.core.domain.repository

import team.bue.bugle.core.model.auth.FindAccountIdRequest
import team.bue.bugle.core.model.auth.FindAccountIdResult
import team.bue.bugle.core.model.auth.LoginRequest
import team.bue.bugle.core.model.auth.ResetPasswordRequest
import team.bue.bugle.core.model.auth.SignUpRequest
import team.bue.bugle.core.model.auth.TokenPair

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<TokenPair>

    suspend fun signUp(request: SignUpRequest): Result<TokenPair>

    suspend fun findAccountId(request: FindAccountIdRequest): Result<FindAccountIdResult>

    suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit>

    suspend fun getSavedTokenPair(): TokenPair?

    suspend fun clearSavedTokenPair()
}
