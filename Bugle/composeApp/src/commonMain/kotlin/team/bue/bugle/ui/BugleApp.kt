package team.bue.bugle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import team.aliens.dms.android.core.designsystem.snackbar.BugleSnackBar
import team.aliens.dms.android.core.designsystem.snackbar.BugleSnackBarVisuals
import team.bue.bugle.feature.emaillogin.ui.EmailLoginScreen
import team.bue.bugle.feature.findpassword.ui.FindPasswordScreen
import team.bue.bugle.feature.onboarding.ui.OnboardingScreen
import team.bue.bugle.feature.signup.ui.SignUpScreen
import team.bue.bugle.feature.splash.ui.SplashScreen
import team.bue.bugle.navigation.EmailLogin
import team.bue.bugle.navigation.FindPassword
import team.bue.bugle.navigation.Home
import team.bue.bugle.navigation.Onboarding
import team.bue.bugle.navigation.SignUp
import team.bue.bugle.navigation.Splash

@Composable
@Suppress("FunctionName")
fun BugleApp(
    modifier: Modifier = Modifier,
    appState: BugleAppState = rememberBugleAppState(),
    pendingKakaoRedirectUri: String? = null,
    onConsumeKakaoRedirect: () -> Unit = {},
) {
    val backStack = remember { mutableStateListOf<Any>(Splash) }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Scaffold(
            modifier = Modifier.systemBarsPadding(),
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                entryProvider =
                    entryProvider {
                        entry<Splash> {
                            SplashScreen(
                                onSplashCompleted = {
                                    backStack.add(Onboarding)
                                },
                            )
                        }
                        entry<Onboarding> {
                            OnboardingScreen(
                                pendingKakaoRedirectUri = pendingKakaoRedirectUri,
                                onConsumeKakaoRedirect = onConsumeKakaoRedirect,
                                onNavigateToHome = {
                                    backStack.add(Home)
                                },
                                onNavigateToEmailLogin = {
                                    backStack.add(EmailLogin)
                                },
                            )
                        }
                        entry<EmailLogin> {
                            EmailLoginScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onNavigateToHome = {
                                    backStack.add(Home)
                                },
                                onNavigateToSignUp = {
                                    backStack.add(SignUp)
                                },
                                onNavigateToFindPassword = {
                                    backStack.add(FindPassword)
                                },
                            )
                        }
                        entry<SignUp> {
                            SignUpScreen(
                                onBack = {
                                    backStack.removeLastOrNull()
                                },
                                onNavigateToHome = {
                                    backStack.add(Home)
                                },
                            )
                        }
                        entry<FindPassword> {
                            FindPasswordScreen(
                                onExit = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        }
                        entry<Home> {
                            Box(modifier = Modifier.fillMaxSize())
                        }
                    },
            )
        }
        SnackbarHost(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
                    .zIndex(2f),
            hostState = appState.snackBarHostState,
            snackbar = {
                val visuals = it.visuals as? BugleSnackBarVisuals ?: return@SnackbarHost
                BugleSnackBar(
                    snackBarType = visuals.snackBarType,
                    message = visuals.message,
                )
            },
        )
    }
}
