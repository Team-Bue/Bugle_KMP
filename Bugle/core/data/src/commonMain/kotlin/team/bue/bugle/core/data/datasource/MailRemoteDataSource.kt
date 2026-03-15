package team.bue.bugle.core.data.datasource

import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeResult
import team.bue.bugle.core.network.dto.mail.toDomain
import team.bue.bugle.core.network.dto.mail.toDto
import team.bue.bugle.core.network.service.MailApiService

interface MailRemoteDataSource {
    suspend fun sendCode(request: SendMailCodeRequest)

    suspend fun verifyCode(request: VerifyMailCodeRequest): VerifyMailCodeResult
}

class MailRemoteDataSourceImpl(
    private val mailApiService: MailApiService,
) : MailRemoteDataSource {
    override suspend fun sendCode(request: SendMailCodeRequest) {
        mailApiService.sendCode(request.toDto())
    }

    override suspend fun verifyCode(request: VerifyMailCodeRequest): VerifyMailCodeResult =
        mailApiService.verifyCode(request.toDto()).toDomain()
}
