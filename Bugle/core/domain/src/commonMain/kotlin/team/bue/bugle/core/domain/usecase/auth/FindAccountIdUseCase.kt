package team.bue.bugle.core.domain.usecase.auth

import team.bue.bugle.core.domain.repository.AuthRepository
import team.bue.bugle.core.model.auth.FindAccountIdRequest
import team.bue.bugle.core.model.auth.FindAccountIdResult

class FindAccountIdUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(request: FindAccountIdRequest): Result<FindAccountIdResult> {
        return authRepository.findAccountId(request)
    }
}
