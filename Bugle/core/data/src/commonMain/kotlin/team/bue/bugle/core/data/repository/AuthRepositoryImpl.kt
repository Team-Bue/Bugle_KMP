package team.bue.bugle.core.data.repository

import co.touchlab.kermit.Logger
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import team.bue.bugle.core.data.datasource.AuthRemoteDataSource
import team.bue.bugle.core.data.datasource.TokenLocalDataSource
import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.LoginRequest
import team.bue.bugle.core.model.auth.ResetPasswordRequest
import team.bue.bugle.core.model.auth.SignUpRequest
import team.bue.bugle.core.model.auth.TokenPair
import team.bue.bugle.core.model.exception.BugleException

class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
) : AuthRepository {
    private val logger = Logger.withTag("AuthRepositoryImpl")

    override suspend fun login(request: LoginRequest): Result<TokenPair> =
        runCatching {
            authRemoteDataSource.login(request).also { tokenPair ->
                tokenLocalDataSource.save(tokenPair)
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it.toBugleException()) },
        )

    override suspend fun signUp(request: SignUpRequest): Result<TokenPair> =
        runCatching {
            authRemoteDataSource.signUp(request).also { tokenPair ->
                tokenLocalDataSource.save(tokenPair)
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it.toBugleException()) },
        )

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit> =
        runCatching {
            authRemoteDataSource.resetPassword(request)
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it.toBugleException()) },
        )

    override suspend fun startKakaoOAuth(): Result<String> =
        runCatching {
            authRemoteDataSource.startKakaoOAuth()
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it.toBugleException()) },
        )

    override suspend fun completeKakaoOAuth(
        code: String,
        state: String?,
    ): Result<TokenPair> =
        runCatching {
            authRemoteDataSource.completeKakaoOAuth(
                code = code,
                state = state,
            ).also { tokenPair ->
                tokenLocalDataSource.save(tokenPair)
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it.toBugleException()) },
        )

    override suspend fun getSavedTokenPair(): TokenPair? = tokenLocalDataSource.get()

    override suspend fun clearSavedTokenPair() {
        tokenLocalDataSource.clear()
    }
}

private fun Throwable.toBugleException(): BugleException =
    when (this) {
        is ClientRequestException ->
            when (response.status.value) {
                401 -> BugleException.InvalidCredentials
                404 -> BugleException.NotFound
                else -> BugleException.Unknown
            }
        is ServerResponseException -> BugleException.ServerError(response.status.value)
        else -> BugleException.NetworkError
    }
