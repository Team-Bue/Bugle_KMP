package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.TokenPair

class GetSavedTokenPairUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): TokenPair? = authRepository.getSavedTokenPair()
}
