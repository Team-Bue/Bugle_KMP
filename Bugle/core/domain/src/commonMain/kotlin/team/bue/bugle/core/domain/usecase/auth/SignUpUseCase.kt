package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.SignUpRequest
import team.bue.bugle.core.model.auth.TokenPair

class SignUpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: SignUpRequest): Result<TokenPair> {
        return authRepository.signUp(request)
    }
}
