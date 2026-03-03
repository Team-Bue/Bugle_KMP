package team.bue.bugle.feature.splash.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import team.bue.bugle.designsystem.foundation.BugleIcon
import team.bue.bugle.feature.splash.viewmodel.SplashSideEffect
import team.bue.bugle.feature.splash.viewmodel.SplashViewModel

@Composable
fun SplashScreen(
    onSplashCompleted: () -> Unit
) {
    val viewModel: SplashViewModel = koinViewModel()
    val scale = remember { Animatable(0.4f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SplashSideEffect.NavigateToMain -> onSplashCompleted()
            }
        }
    }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800),
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow,
            ),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(BugleIcon.AppLogo),
            contentDescription = "bugle app logo",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = alpha.value,
                ),
        )
    }
}
