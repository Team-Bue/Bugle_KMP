package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.TokenPair

class CompleteKakaoOAuthUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        code: String,
        state: String?,
    ): Result<TokenPair> {
        return authRepository.completeKakaoOAuth(
            code = code,
            state = state,
        )
    }
}
