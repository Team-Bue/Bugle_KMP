package team.bue.bugle.feature.emaillogin.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.bue.bugle.core.domain.usecase.auth.LoginUseCase
import team.bue.bugle.core.model.auth.LoginRequest
import team.bue.bugle.core.model.exception.BugleException
import team.bue.bugle.core.ui.BaseViewModel

class EmailLoginViewModel(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<EmailLoginUiState, EmailLoginSideEffect>(EmailLoginUiState()) {

    fun onLoginIdChange(value: String) {
        setState {
            it.copy(
                loginId = value,
                loginIdError = null,
            )
        }
    }

    fun onPasswordChange(value: String) {
        setState {
            it.copy(
                password = value,
                passwordError = null,
            )
        }
    }

    fun onLoginClick() {
        // TEMP: UI 확인용 바이패스
        sendEffect(EmailLoginSideEffect.NavigateToHome)
        return

        val currentState = uiState.value
        if (currentState.loginId.isBlank() || currentState.password.isBlank() || currentState.isLoading) {
            return
        }

        viewModelScope.launch {
            setState { it.copy(isLoading = true, loginIdError = null, passwordError = null) }

            loginUseCase(
                LoginRequest(
                    loginId = currentState.loginId,
                    password = currentState.password,
                ),
            ).onSuccess {
                setState { it.copy(isLoading = false) }
                sendEffect(EmailLoginSideEffect.NavigateToHome)
            }.onFailure { throwable ->
                if (PREVIEW_BYPASS_API_FAILURE) {
                    setState { it.copy(isLoading = false, loginIdError = null, passwordError = null) }
                    sendEffect(EmailLoginSideEffect.NavigateToHome)
                    return@onFailure
                }

                val (loginIdError, passwordError) = when (throwable) {
                    is BugleException.InvalidCredentials -> null to "아이디 또는 비밀번호가 올바르지 않습니다."
                    is BugleException.NotFound -> "존재하지 않는 계정입니다." to null
                    is BugleException.NetworkError -> null to "네트워크 연결을 확인해 주세요."
                    is BugleException.ServerError -> null to "서버에 문제가 발생했습니다."
                    else -> null to DEFAULT_LOGIN_ERROR_MESSAGE
                }
                setState {
                    it.copy(
                        isLoading = false,
                        loginIdError = loginIdError,
                        passwordError = passwordError,
                    )
                }
            }
        }
    }

    companion object {
        private const val PREVIEW_BYPASS_API_FAILURE = true
        private const val DEFAULT_LOGIN_ERROR_MESSAGE = "아이디 또는 비밀번호를 확인해 주세요."
    }
}

data class EmailLoginUiState(
    val loginId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginIdError: String? = null,
    val passwordError: String? = null,
) {
    val isLoginEnabled: Boolean
        get() = true // TEMP: UI 확인용 바이패스
}

sealed interface EmailLoginSideEffect {
    data object NavigateToHome : EmailLoginSideEffect
}
