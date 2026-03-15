package team.bue.bugle.core.model.mail

data class SendMailCodeRequest(
    val email: String,
)

data class VerifyMailCodeRequest(
    val email: String,
    val code: String,
)

data class VerifyMailCodeResult(
    val token: String?,
)
