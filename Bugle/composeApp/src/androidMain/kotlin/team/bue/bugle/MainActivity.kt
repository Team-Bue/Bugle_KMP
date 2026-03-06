package team.bue.bugle

import android.os.Bundle
import android.net.Uri
import co.touchlab.kermit.Logger
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.ui.BugleApp

class MainActivity : ComponentActivity() {
    private companion object {
        const val KEY_PENDING_KAKAO_REDIRECT_URI = "key_pending_kakao_redirect_uri"
    }

    private val pendingKakaoRedirectUri = mutableStateOf<String?>(null)
    private val logger = Logger.withTag("MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        pendingKakaoRedirectUri.value =
            savedInstanceState?.getString(KEY_PENDING_KAKAO_REDIRECT_URI)
                ?: intent?.dataString?.takeIf { it.isKakaoOAuthCallbackUri() }
        logger.i {
            "onCreate pendingKakaoRedirectUri=${pendingKakaoRedirectUri.value?.toSafeLogValue() ?: "null"}"
        }

        setContent {
            BugleTheme {
                BugleApp(
                    pendingKakaoRedirectUri = pendingKakaoRedirectUri.value,
                    onConsumeKakaoRedirect = {
                        pendingKakaoRedirectUri.value = null
                    },
                )
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val receivedUri = intent.dataString
        pendingKakaoRedirectUri.value = receivedUri?.takeIf { it.isKakaoOAuthCallbackUri() }
        if (!receivedUri.isNullOrBlank() && pendingKakaoRedirectUri.value == null) {
            logger.i {
                "onNewIntent ignored non-callback uri=${receivedUri.toSafeLogValue()}"
            }
        }
        logger.i {
            "onNewIntent kakaoRedirectUri=${pendingKakaoRedirectUri.value?.toSafeLogValue() ?: "null"}"
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PENDING_KAKAO_REDIRECT_URI, pendingKakaoRedirectUri.value)
    }
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

private fun String.isKakaoOAuthCallbackUri(): Boolean {
    val uri = Uri.parse(this)
    if (uri.scheme != "https") {
        return false
    }

    val host = uri.host ?: return false
    val path = uri.path.orEmpty()

    val isApiCallback = host == "api.bugle.site" && path.startsWith("/bugle/oauth2/kakao")
    val isBugleTeamCallback = host == "bugle.team" && (path == "/auth/oauth2" || path == "/auth/oauth2/")

    return isApiCallback || isBugleTeamCallback
}
