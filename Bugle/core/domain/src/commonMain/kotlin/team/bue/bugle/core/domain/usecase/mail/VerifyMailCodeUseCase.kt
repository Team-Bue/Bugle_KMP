package team.bue.bugle.core.domain.usecase.mail

import team.bue.bugle.core.domain.repository.MailRepository
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeResult

class VerifyMailCodeUseCase(
    private val mailRepository: MailRepository,
) {
    suspend operator fun invoke(request: VerifyMailCodeRequest): Result<VerifyMailCodeResult> {
        return mailRepository.verifyCode(request)
    }
}
