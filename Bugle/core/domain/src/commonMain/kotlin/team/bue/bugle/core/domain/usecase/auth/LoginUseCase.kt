package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.LoginRequest
import team.bue.bugle.core.model.auth.TokenPair

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: LoginRequest): Result<TokenPair> {
        return authRepository.login(request)
    }
}
