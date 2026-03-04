package team.bue.bugle.core.model.user

data class MyProfile(
    val userId: Long,
    val accountId: String,
    val userName: String,
    val profileImageObjectKey: String?,
)
