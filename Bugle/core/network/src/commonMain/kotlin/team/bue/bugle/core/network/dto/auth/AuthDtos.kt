package team.bue.bugle.core.network.dto.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import team.bue.bugle.core.model.auth.FindAccountIdRequest
import team.bue.bugle.core.model.auth.FindAccountIdResult
import team.bue.bugle.core.model.auth.LoginRequest
import team.bue.bugle.core.model.auth.ResetPasswordRequest
import team.bue.bugle.core.model.auth.SignUpRequest
import team.bue.bugle.core.model.auth.TokenPair

@Serializable
data class LoginRequestDto(
    @SerialName("login_id")
    val loginId: String,
    val password: String,
)

@Serializable
data class SignUpRequestDto(
    val email: String,
    val token: String,
    val password: String,
    @SerialName("account_id")
    val accountId: String,
    @SerialName("user_name")
    val userName: String? = null,
)

@Serializable
data class TokenPairResponseDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
)

@Serializable
data class ResetPasswordRequestDto(
    val email: String,
    val token: String,
    @SerialName("new_password")
    val newPassword: String,
)

@Serializable
data class FindAccountIdRequestDto(
    val email: String,
    val token: String,
)

@Serializable
data class FindAccountIdResponseDto(
    @SerialName("account_id")
    val accountId: String,
)

fun LoginRequest.toDto(): LoginRequestDto =
    LoginRequestDto(
        loginId = loginId,
        password = password,
    )

fun SignUpRequest.toDto(): SignUpRequestDto =
    SignUpRequestDto(
        email = email,
        token = token,
        password = password,
        accountId = accountId,
        userName = userName,
    )

fun ResetPasswordRequest.toDto(): ResetPasswordRequestDto =
    ResetPasswordRequestDto(
        email = email,
        token = token,
        newPassword = newPassword,
    )

fun FindAccountIdRequest.toDto(): FindAccountIdRequestDto =
    FindAccountIdRequestDto(
        email = email,
        token = token,
    )

fun TokenPairResponseDto.toDomain(): TokenPair =
    TokenPair(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )

fun FindAccountIdResponseDto.toDomain(): FindAccountIdResult =
    FindAccountIdResult(accountId = accountId)
