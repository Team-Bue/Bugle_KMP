package team.bue.bugle.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import team.aliens.dms.android.core.designsystem.snackbar.BugleSnackBar
import team.aliens.dms.android.core.designsystem.snackbar.BugleSnackBarVisuals
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.feature.emaillogin.ui.EmailLoginScreen
import team.bue.bugle.feature.onboarding.ui.OnboardingScreen
import team.bue.bugle.feature.resetpassword.ui.ResetPasswordScreen
import team.bue.bugle.feature.signup.ui.SignUpScreen
import team.bue.bugle.feature.splash.ui.SplashScreen
import team.bue.bugle.navigation.EmailLogin
import team.bue.bugle.navigation.Home
import team.bue.bugle.navigation.Onboarding
import team.bue.bugle.navigation.ResetPassword
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
            containerColor = Color.Black,
        ) {
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeLastOrNull() },
                transitionSpec = {
                    (slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth / 6 },
                        animationSpec = tween(durationMillis = 260),
                    ) + fadeIn(animationSpec = tween(durationMillis = 220))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth / 12 },
                            animationSpec = tween(durationMillis = 220),
                        ) + fadeOut(animationSpec = tween(durationMillis = 180)))
                },
                popTransitionSpec = {
                    (slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth / 6 },
                        animationSpec = tween(durationMillis = 240),
                    ) + fadeIn(animationSpec = tween(durationMillis = 200))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth / 10 },
                            animationSpec = tween(durationMillis = 220),
                        ) + fadeOut(animationSpec = tween(durationMillis = 170)))
                },
                predictivePopTransitionSpec = {
                    (slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth / 6 },
                        animationSpec = tween(durationMillis = 220),
                    ) + fadeIn(animationSpec = tween(durationMillis = 180))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth / 10 },
                            animationSpec = tween(durationMillis = 220),
                        ) + fadeOut(animationSpec = tween(durationMillis = 170)))
                },
                entryProvider =
                    entryProvider {
                        entry<Splash> {
                            SplashScreen(
                                onNavigateToOnboarding = {
                                    backStack.clear()
                                    backStack.add(Onboarding)
                                },
                                onNavigateToHome = {
                                    backStack.clear()
                                    backStack.add(Home)
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
                                onNavigateToResetPassword = {
                                    backStack.add(ResetPassword)
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
                        entry<ResetPassword> {
                            ResetPasswordScreen(
                                onExit = {
                                    backStack.removeLastOrNull()
                                },
                            )
                        }
                        entry<Home> {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .background(Color.Black),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                BasicText(
                                    text = "로그인은 완료되었어요",
                                    style = BugleTypography.titleL.copy(color = Color.White),
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                BasicText(
                                    text = "현재 Home 화면은 아직 연결되지 않아 임시 화면을 보여주고 있어요.",
                                    style = BugleTypography.textM.copy(
                                        color = Color(0xFFB3B3B3),
                                        textAlign = TextAlign.Center,
                                    ),
                                )
                            }
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
