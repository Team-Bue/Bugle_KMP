package team.bue.bugle.feature.findpassword.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.bue.bugle.core.domain.usecase.auth.ResetPasswordUseCase
import team.bue.bugle.core.domain.usecase.mail.SendMailCodeUseCase
import team.bue.bugle.core.domain.usecase.mail.VerifyMailCodeUseCase
import team.bue.bugle.core.model.auth.ResetPasswordRequest
import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.ui.BaseViewModel

enum class FindPasswordStep {
    ENTER_EMAIL,
    VERIFY_CODE,
    RESET_PASSWORD,
}

class FindPasswordViewModel(
    private val sendMailCodeUseCase: SendMailCodeUseCase,
    private val verifyMailCodeUseCase: VerifyMailCodeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
) : BaseViewModel<FindPasswordUiState, FindPasswordSideEffect>(FindPasswordUiState()) {

    fun onEmailChange(value: String) = setState { it.copy(email = value, emailError = null) }

    fun onCodeChange(value: String) = setState { it.copy(code = value, codeError = null) }

    fun onPasswordChange(value: String) = setState { it.copy(password = value, passwordError = null, submitError = null) }

    fun onPasswordConfirmChange(value: String) =
        setState { it.copy(passwordConfirm = value, passwordError = null, submitError = null) }

    fun onBack() {
        when (uiState.value.step) {
            FindPasswordStep.ENTER_EMAIL -> sendEffect(FindPasswordSideEffect.Exit)
            FindPasswordStep.VERIFY_CODE -> setState { it.copy(step = FindPasswordStep.ENTER_EMAIL, codeError = null) }
            FindPasswordStep.RESET_PASSWORD -> setState { it.copy(step = FindPasswordStep.VERIFY_CODE, passwordError = null) }
        }
    }

    fun onSendCode() {
        val state = uiState.value
        if (state.isLoading) return
        if (!EMAIL_REGEX.matches(state.email)) {
            setState { it.copy(emailError = "올바른 이메일 형식이 아닙니다.") }
            return
        }

        viewModelScope.launch {
            setState { it.copy(isLoading = true, emailError = null) }
            sendMailCodeUseCase(SendMailCodeRequest(email = state.email)).onSuccess {
                setState {
                    it.copy(
                        isLoading = false,
                        step = FindPasswordStep.VERIFY_CODE,
                        verificationToken = null,
                    )
                }
            }.onFailure {
                setState { it.copy(isLoading = false, emailError = "인증 코드 발송에 실패했습니다.") }
            }
        }
    }

    fun onVerifyCode() {
        val state = uiState.value
        if (state.isLoading) return
        if (!CODE_REGEX.matches(state.code)) {
            setState { it.copy(codeError = "인증 코드는 6자리여야 합니다.") }
            return
        }

        viewModelScope.launch {
            setState { it.copy(isLoading = true, codeError = null) }
            verifyMailCodeUseCase(
                VerifyMailCodeRequest(
                    email = state.email,
                    code = state.code,
                ),
            ).onSuccess { result ->
                val token = result.token
                if (token.isNullOrBlank()) {
                    setState { it.copy(isLoading = false, codeError = "인증 토큰을 받지 못했습니다.") }
                } else {
                    setState {
                        it.copy(
                            isLoading = false,
                            step = FindPasswordStep.RESET_PASSWORD,
                            verificationToken = token,
                        )
                    }
                }
            }.onFailure {
                setState { it.copy(isLoading = false, codeError = "인증 코드가 올바르지 않습니다.") }
            }
        }
    }

    fun onResetPassword() {
        val state = uiState.value
        if (state.isLoading) return
        if (!PASSWORD_REGEX.matches(state.password)) {
            setState { it.copy(passwordError = "비밀번호는 8~30자이며 특수문자(@#!%&*)를 포함해야 합니다.") }
            return
        }
        if (state.password != state.passwordConfirm) {
            setState { it.copy(passwordError = "비밀번호가 일치하지 않습니다.") }
            return
        }

        val token = state.verificationToken
        if (token.isNullOrBlank()) {
            setState { it.copy(submitError = "이메일 인증을 다시 진행해 주세요.") }
            return
        }

        viewModelScope.launch {
            setState { it.copy(isLoading = true, submitError = null) }
            resetPasswordUseCase(
                ResetPasswordRequest(
                    email = state.email,
                    token = token,
                    newPassword = state.password,
                ),
            ).onSuccess {
                setState { it.copy(isLoading = false) }
                sendEffect(FindPasswordSideEffect.Exit)
            }.onFailure {
                setState { it.copy(isLoading = false, submitError = "비밀번호 재설정에 실패했습니다.") }
            }
        }
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        private val CODE_REGEX = Regex("^.{6}$")
        private val PASSWORD_REGEX = Regex("^(?=.*[@#!%&*])[A-Za-z0-9@#!%&*]{8,30}$")
    }
}

data class FindPasswordUiState(
    val step: FindPasswordStep = FindPasswordStep.ENTER_EMAIL,
    val email: String = "",
    val code: String = "",
    val verificationToken: String? = null,
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val codeError: String? = null,
    val passwordError: String? = null,
    val submitError: String? = null,
)

sealed interface FindPasswordSideEffect {
    data object Exit : FindPasswordSideEffect
}
