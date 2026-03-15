package team.bue.bugle.core.domain.usecase.mail

import team.bue.bugle.core.domain.repository.MailRepository
import team.bue.bugle.core.model.mail.SendMailCodeRequest

class SendMailCodeUseCase(
    private val mailRepository: MailRepository,
) {
    suspend operator fun invoke(request: SendMailCodeRequest): Result<Unit> {
        return mailRepository.sendCode(request)
    }
}
