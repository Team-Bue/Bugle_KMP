package team.bue.bugle.feature.onboarding.viewmodel

import co.touchlab.kermit.Logger
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import team.bue.bugle.core.domain.usecase.auth.CompleteKakaoOAuthUseCase
import team.bue.bugle.core.domain.usecase.auth.StartKakaoOAuthUseCase
import team.bue.bugle.core.model.exception.BugleException
import team.bue.bugle.core.ui.BaseViewModel

class OnboardingViewModel(
    private val startKakaoOAuthUseCase: StartKakaoOAuthUseCase,
    private val completeKakaoOAuthUseCase: CompleteKakaoOAuthUseCase,
) : BaseViewModel<OnboardingUiState, OnboardingSideEffect>(OnboardingUiState()) {
    private val logger = Logger.withTag("OnboardingViewModel")

    fun onKakaoLoginClick() {
        logger.i { "Kakao login button clicked. isLoading=${uiState.value.isLoading}" }
        if (uiState.value.isLoading) {
            logger.i { "Kakao login ignored because loading is in progress." }
            return
        }

        viewModelScope.launch {
            setState {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    isKakaoLoginFlowStarted = false,
                    expectedKakaoState = null,
                )
            }

            startKakaoOAuthUseCase()
                .onSuccess { loginUrl ->
                    val normalizedLoginUrl = loginUrl.normalizedKakaoLoginStartUrl()
                    logger.i { "Start Kakao OAuth succeeded. loginUrl=${normalizedLoginUrl.toSafeLogValue()}" }
                    val expectedState = normalizedLoginUrl.parseQueryParams()["state"]
                    if (expectedState.isNullOrBlank()) {
                        logger.i { "Start Kakao OAuth URL has no state. callback state will be validated by backend." }
                    }
                    setState {
                        it.copy(
                            isLoading = false,
                            expectedKakaoState = expectedState,
                            isKakaoLoginFlowStarted = true,
                        )
                    }
                    logger.i { "Opening Kakao OAuth URL." }
                    sendEffect(OnboardingSideEffect.OpenKakaoLogin(normalizedLoginUrl))
                }
                .onFailure { throwable ->
                    logger.i { "Start Kakao OAuth failed: ${throwable::class.simpleName}" }
                    setState {
                        it.copy(
                            isLoading = false,
                            expectedKakaoState = null,
                            isKakaoLoginFlowStarted = false,
                            errorMessage = throwable.toMessage(),
                        )
                    }
                }
        }
    }

    fun handleKakaoRedirect(uri: String) {
        logger.i { "Received Kakao redirect URI=${uri.toSafeLogValue()}" }
        if (uiState.value.isLoading) {
            logger.i { "Redirect ignored because loading is in progress." }
            return
        }

        if (!uiState.value.isKakaoLoginFlowStarted) {
            logger.i { "Redirect rejected: kakao login flow not started." }
            setState {
                it.copy(
                    isLoading = false,
                    expectedKakaoState = null,
                    isKakaoLoginFlowStarted = false,
                    errorMessage = "카카오 로그인 요청 이후에만 콜백을 처리할 수 있습니다.",
                )
            }
            return
        }

        val redirectInfo = uri.parseRedirectInfo()
        if (!redirectInfo.isAllowedKakaoCallback()) {
            logger.i {
                "Redirect rejected: not in allowlist. scheme=${redirectInfo.scheme}, host=${redirectInfo.host}, path=${redirectInfo.path}"
            }
            setState {
                it.copy(
                    isLoading = false,
                    expectedKakaoState = null,
                    isKakaoLoginFlowStarted = false,
                    errorMessage = "유효하지 않은 카카오 로그인 콜백입니다.",
                )
            }
            return
        }

        val queryParams = uri.parseQueryParams()
        val error = queryParams["error"]
        val errorDescription = queryParams["error_description"]

        if (error != null) {
            logger.i { "Redirect contains oauth error=$error" }
            setState {
                it.copy(
                    isLoading = false,
                    expectedKakaoState = null,
                    isKakaoLoginFlowStarted = false,
                    errorMessage = errorDescription ?: "카카오 로그인이 취소되었거나 실패했습니다.",
                )
            }
            return
        }

        val expectedState = uiState.value.expectedKakaoState
        val callbackState = queryParams["state"]

        if (!expectedState.isNullOrBlank() && expectedState != callbackState) {
            logger.i {
                "Redirect rejected by state validation. expectedStatePresent=${!expectedState.isNullOrBlank()}, callbackStatePresent=${!callbackState.isNullOrBlank()}"
            }
            setState {
                it.copy(
                    isLoading = false,
                    expectedKakaoState = null,
                    isKakaoLoginFlowStarted = false,
                    errorMessage = "카카오 로그인 검증에 실패했습니다.",
                )
            }
            return
        }

        val code = queryParams["code"]
        if (code.isNullOrBlank()) {
            logger.i { "Redirect rejected: code parameter missing." }
            setState {
                it.copy(
                    isLoading = false,
                    expectedKakaoState = null,
                    isKakaoLoginFlowStarted = false,
                    errorMessage = "인가 코드가 없어 카카오 로그인을 완료할 수 없습니다.",
                )
            }
            return
        }

        viewModelScope.launch {
            logger.i { "Completing Kakao OAuth with backend finalize API." }
            setState { it.copy(isLoading = true, errorMessage = null) }
            completeKakaoOAuthUseCase(
                code = code,
                state = callbackState,
            )
                .onSuccess {
                    logger.i { "Complete Kakao OAuth succeeded. Navigating to home." }
                    setState {
                        it.copy(
                            isLoading = false,
                            expectedKakaoState = null,
                            isKakaoLoginFlowStarted = false,
                        )
                    }
                    sendEffect(OnboardingSideEffect.NavigateToHome)
                }
                .onFailure { throwable ->
                    logger.i { "Complete Kakao OAuth failed: ${throwable::class.simpleName}" }
                    setState {
                        it.copy(
                            isLoading = false,
                            expectedKakaoState = null,
                            isKakaoLoginFlowStarted = false,
                            errorMessage = throwable.toMessage(),
                        )
                    }
                }
        }
    }

    fun onEmailLoginClick() {
        sendEffect(OnboardingSideEffect.NavigateToEmailLogin)
    }
}

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val expectedKakaoState: String? = null,
    val isKakaoLoginFlowStarted: Boolean = false,
)

sealed interface OnboardingSideEffect {
    data class OpenKakaoLogin(val url: String) : OnboardingSideEffect
    data object NavigateToHome : OnboardingSideEffect
    data object NavigateToEmailLogin : OnboardingSideEffect
}

private fun String.parseQueryParams(): Map<String, String> {
    val query = substringAfter('?', missingDelimiterValue = "").substringBefore('#')
    if (query.isBlank() && !contains('#')) {
        return emptyMap()
    }

    val fragment = substringAfter('#', missingDelimiterValue = "")
    val merged = listOf(query, fragment)
        .filter { it.contains('=') }
        .joinToString("&")

    if (merged.isBlank()) {
        return emptyMap()
    }

    return merged
        .split('&')
        .mapNotNull { keyValue ->
            val key = keyValue.substringBefore('=', missingDelimiterValue = "")
            if (key.isBlank()) {
                null
            } else {
                key to keyValue.substringAfter('=', missingDelimiterValue = "")
            }
        }
        .toMap()
}

private fun Throwable.toMessage(): String =
    when (this) {
        is BugleException.NetworkError -> "네트워크 연결을 확인해 주세요."
        is BugleException.ServerError -> "서버에 문제가 발생했습니다."
        else -> "카카오 로그인에 실패했습니다."
    }

private data class RedirectInfo(
    val scheme: String,
    val host: String,
    val path: String,
)

private fun String.parseRedirectInfo(): RedirectInfo {
    val normalized = trim()
    val scheme = normalized.substringBefore(':', missingDelimiterValue = "").lowercase()
    val remainder = normalized.substringAfter(':', missingDelimiterValue = "")
        .trimStart('/')

    val authority = remainder
        .substringBefore('/', missingDelimiterValue = remainder)
        .substringBefore('?', missingDelimiterValue = remainder)
        .substringBefore('#', missingDelimiterValue = remainder)
    val host = authority
        .substringBefore('@', missingDelimiterValue = authority)
        .substringBefore(':', missingDelimiterValue = authority)
        .lowercase()

    val pathStartIndex = remainder.indexOf('/').takeIf { it >= 0 }
    val path = if (pathStartIndex == null) {
        ""
    } else {
        "/" + remainder
            .substring(pathStartIndex + 1)
            .substringBefore('?', missingDelimiterValue = "")
            .substringBefore('#', missingDelimiterValue = "")
            .trimStart('/')
    }

    return RedirectInfo(
        scheme = scheme,
        host = host,
        path = path,
    )
}

private fun RedirectInfo.isAllowedKakaoCallback(): Boolean {
    if (scheme != "https") {
        return false
    }

    val isApiHost = host == "api.bugle.site" && path.startsWith("/bugle/oauth2/kakao")
    val isAppHost = host == "bugle.team" && (path == "/auth/oauth2" || path == "/auth/oauth2/")

    return isApiHost || isAppHost
}

private fun String.toSafeLogValue(): String {
    val scheme = substringBefore("://", missingDelimiterValue = "")
    val afterScheme = substringAfter("://", missingDelimiterValue = "")
    val host = afterScheme
        .substringBefore('/', missingDelimiterValue = afterScheme)
        .substringBefore('?', missingDelimiterValue = afterScheme)
        .substringBefore('#', missingDelimiterValue = afterScheme)
    val path = if ('/' in afterScheme) {
        "/" + afterScheme
            .substringAfter('/', missingDelimiterValue = "")
            .substringBefore('?', missingDelimiterValue = "")
            .substringBefore('#', missingDelimiterValue = "")
            .trimStart('/')
    } else {
        ""
    }
    return "$scheme://$host$path"
}

private fun String.normalizedKakaoLoginStartUrl(): String {
    val trimmed = trim()
    val query = trimmed.substringAfter('?', missingDelimiterValue = "")
    val fragment = trimmed.substringAfter('#', missingDelimiterValue = "")
    val withoutQueryOrFragment = trimmed
        .substringBefore('?')
        .substringBefore('#')
        .trimEnd('/')

    return buildString {
        append(withoutQueryOrFragment)
        if (query.isNotEmpty()) {
            append('?')
            append(query)
        }
        if (fragment.isNotEmpty()) {
            append('#')
            append(fragment)
        }
    }
}
