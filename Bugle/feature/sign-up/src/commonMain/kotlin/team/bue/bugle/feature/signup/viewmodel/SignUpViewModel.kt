package team.bue.bugle.feature.signup.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.bue.bugle.core.model.auth.SignUpRequest
import team.bue.bugle.core.model.mail.SendMailCodeRequest
import team.bue.bugle.core.model.mail.VerifyMailCodeRequest
import team.bue.bugle.core.domain.usecase.auth.SignUpUseCase
import team.bue.bugle.core.domain.usecase.mail.SendMailCodeUseCase
import team.bue.bugle.core.domain.usecase.mail.VerifyMailCodeUseCase
import team.bue.bugle.core.ui.BaseViewModel

enum class SignUpStep {
    ENTER_EMAIL,
    VERIFY_EMAIL,
    SET_USERNAME,
    SET_ACCOUNT_ID,
    SET_PASSWORD,
    AGREE_TERMS,
    COMPLETE,
}

class SignUpViewModel(
    private val sendMailCodeUseCase: SendMailCodeUseCase,
    private val verifyMailCodeUseCase: VerifyMailCodeUseCase,
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpUiState, SignUpSideEffect>(SignUpUiState()) {

    fun onEmailChange(value: String) = setState { it.copy(email = value, emailError = null) }

    fun onVerificationCodeChange(value: String) = setState { it.copy(verificationCode = value, codeError = null) }

    fun onPasswordChange(value: String) = setState { it.copy(password = value, passwordError = null) }

    fun onPasswordConfirmChange(value: String) =
        setState { it.copy(passwordConfirm = value, passwordError = null) }

    fun onAccountIdChange(value: String) = setState { it.copy(accountId = value, accountIdError = null) }

    fun onUserNameChange(value: String) = setState { it.copy(userName = value, userNameError = null, signUpError = null) }

    fun onBack() {
        val previous = when (uiState.value.step) {
            SignUpStep.VERIFY_EMAIL -> SignUpStep.ENTER_EMAIL
            SignUpStep.SET_USERNAME -> SignUpStep.VERIFY_EMAIL
            SignUpStep.SET_ACCOUNT_ID -> SignUpStep.SET_USERNAME
            SignUpStep.SET_PASSWORD -> SignUpStep.SET_ACCOUNT_ID
            SignUpStep.AGREE_TERMS -> SignUpStep.SET_PASSWORD
            else -> null
        }
        if (previous != null) {
            setState { it.copy(step = previous) }
        } else {
            setState { it.copy(showExitDialog = true) }
        }
    }

    fun onSystemBack() {
        setState { it.copy(showExitDialog = true) }
    }

    fun onExitDialogDismiss() {
        setState { it.copy(showExitDialog = false) }
    }

    fun onExitDialogConfirm() {
        setState { SignUpUiState() }
        sendEffect(SignUpSideEffect.NavigateBack)
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
            sendMailCodeUseCase(SendMailCodeRequest(email = state.email))
                .onSuccess {
                    setState {
                        it.copy(
                            isLoading = false,
                            step = SignUpStep.VERIFY_EMAIL,
                            verificationToken = null,
                        )
                    }
                }
                .onFailure {
                    setState { it.copy(isLoading = false, emailError = "인증 코드 발송에 실패했습니다.") }
                }
        }
    }

    fun onVerifyCode() {
        val state = uiState.value
        if (state.isLoading) return
        if (!VERIFICATION_CODE_REGEX.matches(state.verificationCode)) {
            setState { it.copy(codeError = "인증 코드는 6자리여야 합니다.") }
            return
        }


        viewModelScope.launch {
            setState { it.copy(isLoading = true, codeError = null) }
            verifyMailCodeUseCase(
                VerifyMailCodeRequest(
                    email = state.email,
                    code = state.verificationCode,
                ),
            ).onSuccess { result ->
                val token = result.token
                if (token.isNullOrBlank()) {
                    setState { it.copy(isLoading = false, codeError = "인증 토큰을 받지 못했습니다.") }
                } else {
                    setState {
                        it.copy(
                            isLoading = false,
                            step = SignUpStep.SET_USERNAME,
                            verificationToken = token,
                        )
                    }
                }
            }.onFailure {
                setState { it.copy(isLoading = false, codeError = "인증 코드가 올바르지 않습니다.") }
            }
        }
    }

    fun onNextFromUserName() {
        val userName = uiState.value.userName
        if (!USER_NAME_REGEX.matches(userName)) {
            setState { it.copy(userNameError = "이름은 한글/영문만 20자 이하로 입력해주세요.") }
            return
        }
        setState { it.copy(step = SignUpStep.SET_ACCOUNT_ID) }
    }

    fun onNextFromAccountId() {
        val accountId = uiState.value.accountId
        if (!ACCOUNT_ID_REGEX.matches(accountId)) {
            setState {
                it.copy(
                    accountIdError = "아이디는 4~20자, 소문자/숫자/_/.만 가능하며 .으로 시작/종료할 수 없습니다.",
                )
            }
            return
        }
        setState { it.copy(step = SignUpStep.SET_PASSWORD) }
    }

    fun onNextFromPassword() {
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

        val signUpToken = state.verificationToken ?: run {
            setState { it.copy(signUpError = "이메일 인증을 다시 진행해 주세요.") }
            return
        }


        setState { it.copy(step = SignUpStep.AGREE_TERMS, passwordError = null, signUpError = null) }
    }

    fun onToggleAllTerms() {
        val state = uiState.value
        val target = !state.isAllTermsAgreed
        setState {
            it.copy(
                isAgreedServiceTerm = target,
                isAgreedPrivacyTerm = target,
                isAgreedMarketingTerm = target,
                signUpError = null,
            )
        }
    }

    fun onToggleServiceTerm() = setState {
        it.copy(
            isAgreedServiceTerm = !it.isAgreedServiceTerm,
            signUpError = null,
        )
    }

    fun onTogglePrivacyTerm() = setState {
        it.copy(
            isAgreedPrivacyTerm = !it.isAgreedPrivacyTerm,
            signUpError = null,
        )
    }

    fun onToggleMarketingTerm() = setState {
        it.copy(
            isAgreedMarketingTerm = !it.isAgreedMarketingTerm,
            signUpError = null,
        )
    }

    fun onAgreeServiceTerm() = setState {
        it.copy(
            isAgreedServiceTerm = true,
            signUpError = null,
        )
    }

    fun onAgreePrivacyTerm() = setState {
        it.copy(
            isAgreedPrivacyTerm = true,
            signUpError = null,
        )
    }

    fun onAgreeMarketingTerm() = setState {
        it.copy(
            isAgreedMarketingTerm = true,
            signUpError = null,
        )
    }

    fun onSignUp() {
        val state = uiState.value
        if (state.isLoading) return

        if (!state.isRequiredTermsAgreed) {
            setState { it.copy(signUpError = "필수 약관에 동의해주세요.") }
            return
        }

        val signUpToken = state.verificationToken ?: run {
            setState { it.copy(signUpError = "이메일 인증을 다시 진행해 주세요.") }
            return
        }

        viewModelScope.launch {
            setState { it.copy(isLoading = true, signUpError = null) }
            signUpUseCase(
                SignUpRequest(
                    email = state.email,
                    token = signUpToken,
                    password = state.password,
                    accountId = state.accountId,
                    userName = state.userName.ifBlank { null },
                ),
            ).onSuccess {
                setState { it.copy(isLoading = false, step = SignUpStep.COMPLETE) }
            }.onFailure {
                setState { it.copy(isLoading = false, signUpError = "회원가입에 실패했습니다. 다시 시도해 주세요.") }
            }
        }
    }

    fun onCompleteClick() {
        sendEffect(SignUpSideEffect.NavigateToHome)
    }

    companion object {
        private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        private val VERIFICATION_CODE_REGEX = Regex("^.{6}$")
        private val PASSWORD_REGEX = Regex("^(?=.*[@#!%&*])[A-Za-z0-9@#!%&*]{8,30}$")
        private val ACCOUNT_ID_REGEX = Regex("^[a-z0-9]([a-z0-9_.]{2,18}[a-z0-9])$")
        private val USER_NAME_REGEX = Regex("^[A-Za-z가-힣]{1,20}$")
    }
}

data class SignUpUiState(
    val step: SignUpStep = SignUpStep.AGREE_TERMS,
    val email: String = "",
    val verificationCode: String = "",
    val verificationToken: String? = null,
    val password: String = "",
    val passwordConfirm: String = "",
    val accountId: String = "",
    val userName: String = "",
    val isAgreedServiceTerm: Boolean = false,
    val isAgreedPrivacyTerm: Boolean = false,
    val isAgreedMarketingTerm: Boolean = false,
    val isLoading: Boolean = false,
    val showExitDialog: Boolean = false,
    val emailError: String? = null,
    val codeError: String? = null,
    val passwordError: String? = null,
    val accountIdError: String? = null,
    val userNameError: String? = null,
    val signUpError: String? = null,
) {
    val isRequiredTermsAgreed: Boolean
        get() = isAgreedServiceTerm && isAgreedPrivacyTerm

    val isAllTermsAgreed: Boolean
        get() = isRequiredTermsAgreed && isAgreedMarketingTerm
}

sealed interface SignUpSideEffect {
    data object NavigateBack : SignUpSideEffect
    data object NavigateToHome : SignUpSideEffect
}
