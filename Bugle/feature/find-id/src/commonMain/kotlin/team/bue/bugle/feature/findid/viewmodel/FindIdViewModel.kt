package team.bue.bugle.feature.findid.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.bue.bugle.core.domain.usecase.auth.FindAccountIdUseCase
import team.bue.bugle.core.domain.usecase.mail.SendMailCodeUseCase
import team.bue.bugle.core.domain.usecase.mail.VerifyMailCodeUseCase
import team.bue.bugle.core.model.auth.FindAccountIdRequest
import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.ui.BaseViewModel

enum class FindIdStep {
    ENTER_EMAIL,
    VERIFY_CODE,
    RESULT,
}

class FindIdViewModel(
    private val sendMailCodeUseCase: SendMailCodeUseCase,
    private val verifyMailCodeUseCase: VerifyMailCodeUseCase,
    private val findAccountIdUseCase: FindAccountIdUseCase,
) : BaseViewModel<FindIdUiState, FindIdSideEffect>(FindIdUiState()) {

    fun onEmailChange(value: String) = setState { it.copy(email = value, emailError = null) }

    fun onCodeChange(value: String) = setState { it.copy(code = value, codeError = null) }

    fun onBack() {
        when (uiState.value.step) {
            FindIdStep.ENTER_EMAIL -> sendEffect(FindIdSideEffect.Exit)
            FindIdStep.VERIFY_CODE -> setState { it.copy(step = FindIdStep.ENTER_EMAIL, codeError = null) }
            FindIdStep.RESULT -> setState { it.copy(step = FindIdStep.VERIFY_CODE) }
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
                        step = FindIdStep.VERIFY_CODE,
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
                    return@onSuccess
                }

                findAccountIdUseCase(
                    FindAccountIdRequest(
                        email = state.email,
                        token = token,
                    ),
                ).onSuccess { findResult ->
                    setState {
                        it.copy(
                            isLoading = false,
                            verificationToken = token,
                            foundAccountId = findResult.accountId,
                            step = FindIdStep.RESULT,
                        )
                    }
                }.onFailure {
                    setState { it.copy(isLoading = false, codeError = "아이디 조회에 실패했습니다.") }
                }
            }.onFailure {
                setState { it.copy(isLoading = false, codeError = "인증 코드가 올바르지 않습니다.") }
            }
        }
    }

    fun onComplete() {
        sendEffect(FindIdSideEffect.Exit)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        private val CODE_REGEX = Regex("^.{6}$")
    }
}

data class FindIdUiState(
    val step: FindIdStep = FindIdStep.ENTER_EMAIL,
    val email: String = "",
    val code: String = "",
    val verificationToken: String? = null,
    val foundAccountId: String? = null,
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val codeError: String? = null,
)

sealed interface FindIdSideEffect {
    data object Exit : FindIdSideEffect
}
