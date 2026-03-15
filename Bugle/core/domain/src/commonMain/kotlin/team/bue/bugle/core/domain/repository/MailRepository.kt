package team.bue.bugle.core.domain.repository

import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeResult

interface MailRepository {
    suspend fun sendCode(request: SendMailCodeRequest): Result<Unit>

    suspend fun verifyCode(request: VerifyMailCodeRequest): Result<VerifyMailCodeResult>
}
