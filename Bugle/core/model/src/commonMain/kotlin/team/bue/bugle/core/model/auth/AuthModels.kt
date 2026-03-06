package team.bue.bugle.core.model.auth

data class LoginRequest(
    val loginId: String,
    val password: String,
)

data class SignUpRequest(
    val email: String,
    val token: String,
    val password: String,
    val accountId: String,
    val userName: String? = null,
)

data class ResetPasswordRequest(
    val email: String,
    val token: String,
    val newPassword: String,
)

data class TokenPair(
    val accessToken: String,
    val refreshToken: String,
)
