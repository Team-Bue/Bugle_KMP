package team.bue.bugle.core.model.exception

sealed class BugleException : Exception() {
    data object InvalidCredentials : BugleException()

    data object NotFound : BugleException()

    data object NetworkError : BugleException()

    data class ServerError(val code: Int) : BugleException()

    data object Unknown : BugleException()
}
