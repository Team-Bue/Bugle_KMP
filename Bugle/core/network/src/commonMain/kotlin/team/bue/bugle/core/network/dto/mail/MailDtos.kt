package team.bue.bugle.core.network.dto.mail

import kotlinx.serialization.Serializable
import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeResult

@Serializable
data class SendMailCodeRequestDto(
    val email: String,
)

@Serializable
data class VerifyMailCodeRequestDto(
    val email: String,
    val code: String,
)

@Serializable
data class VerifyMailCodeResponseDto(
    val token: String? = null,
)

fun SendMailCodeRequest.toDto(): SendMailCodeRequestDto = SendMailCodeRequestDto(email = email)

fun VerifyMailCodeRequest.toDto(): VerifyMailCodeRequestDto {
    return VerifyMailCodeRequestDto(
        email = email,
        code = code,
    )
}

fun VerifyMailCodeResponseDto.toDomain(): VerifyMailCodeResult = VerifyMailCodeResult(token = token)
