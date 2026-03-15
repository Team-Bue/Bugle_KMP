package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.ResetPasswordRequest

class ResetPasswordUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: ResetPasswordRequest): Result<Unit> {
        return authRepository.resetPassword(request)
    }
}
