package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository

class StartKakaoOAuthUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(): Result<String> {
        return authRepository.startKakaoOAuth()
    }
}
