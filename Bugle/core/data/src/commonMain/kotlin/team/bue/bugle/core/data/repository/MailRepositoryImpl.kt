package team.bue.bugle.core.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import team.bue.bugle.core.data.datasource.MailRemoteDataSource
import team.bue.bugle.core.domain.repository.MailRepository
import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeResult

class MailRepositoryImpl(
    private val mailRemoteDataSource: MailRemoteDataSource,
) : MailRepository {
    override suspend fun sendCode(request: SendMailCodeRequest): Result<Unit> {
        println("[SignUp-Mail][sendCode] request email=${request.email}")
        return runCatching {
            mailRemoteDataSource.sendCode(request)
        }.onSuccess {
            println("[SignUp-Mail][sendCode] success email=${request.email}")
        }.onFailure { throwable ->
            println("[SignUp-Mail][sendCode] failure email=${request.email} reason=${throwable::class.simpleName}:${throwable.message}")
            logHttpFailure("sendCode", throwable)
        }
    }

    override suspend fun verifyCode(request: VerifyMailCodeRequest): Result<VerifyMailCodeResult> =
        runCatching {
            mailRemoteDataSource.verifyCode(request)
        }.onFailure { throwable ->
            println("[SignUp-Mail][verifyCode] failure email=${request.email} reason=${throwable::class.simpleName}:${throwable.message}")
            logHttpFailure("verifyCode", throwable)
        }

    private suspend fun logHttpFailure(apiName: String, throwable: Throwable) {
        when (throwable) {
            is ClientRequestException -> {
                val body = runCatching { throwable.response.bodyAsText() }.getOrNull().orEmpty()
                println("[SignUp-Mail][$apiName] client error status=${throwable.response.status.value} body=$body")
            }

            is ServerResponseException -> {
                val body = runCatching { throwable.response.bodyAsText() }.getOrNull().orEmpty()
                println("[SignUp-Mail][$apiName] server error status=${throwable.response.status.value} body=$body")
            }
        }
    }
}
