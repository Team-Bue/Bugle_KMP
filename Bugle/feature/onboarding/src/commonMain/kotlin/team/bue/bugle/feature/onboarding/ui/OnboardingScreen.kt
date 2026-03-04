package team.bue.bugle.feature.onboarding.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bugle.feature.onboarding.generated.resources.Res
import bugle.feature.onboarding.generated.resources.img_onboarding_map_1
import bugle.feature.onboarding.generated.resources.img_onboarding_map_2
import bugle.feature.onboarding.generated.resources.img_onboarding_photo_1
import bugle.feature.onboarding.generated.resources.img_onboarding_photo_2
import bugle.feature.onboarding.generated.resources.img_onboarding_photo_3
import bugle.feature.onboarding.generated.resources.img_onboarding_photo_4
import bugle.feature.onboarding.generated.resources.img_onboarding_trip_1
import bugle.feature.onboarding.generated.resources.img_onboarding_trip_2
import bugle.feature.onboarding.generated.resources.img_onboarding_trip_3
import bugle.feature.onboarding.generated.resources.img_onboarding_trip_4
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import team.bue.bugle.designsystem.button.BugleSocialButton
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleIcon
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.feature.onboarding.viewmodel.OnboardingSideEffect
import team.bue.bugle.feature.onboarding.viewmodel.OnboardingViewModel
import kotlin.math.absoluteValue

private val CardShape = RoundedCornerShape(32.dp)

private data class OnboardingCardData(
    val emoji: String,
    val label: String,
    val image: DrawableResource,
)

private val onboardingCards = listOf(
    OnboardingCardData("🧭", "Map", Res.drawable.img_onboarding_map_1),
    OnboardingCardData("📍", "Trip", Res.drawable.img_onboarding_trip_1),
    OnboardingCardData("📷", "Photo", Res.drawable.img_onboarding_photo_1),
    OnboardingCardData("📍", "Trip", Res.drawable.img_onboarding_trip_2),
    OnboardingCardData("📷", "Photo", Res.drawable.img_onboarding_photo_2),
    OnboardingCardData("🧭", "Map", Res.drawable.img_onboarding_map_2),
    OnboardingCardData("📍", "Trip", Res.drawable.img_onboarding_trip_3),
    OnboardingCardData("📷", "Photo", Res.drawable.img_onboarding_photo_3),
    OnboardingCardData("📍", "Trip", Res.drawable.img_onboarding_trip_4),
    OnboardingCardData("📷", "Photo", Res.drawable.img_onboarding_photo_4),
)

private const val AUTO_SCROLL_DELAY = 3000L
private const val VIRTUAL_PAGE_COUNT = Int.MAX_VALUE

@Composable
fun OnboardingScreen(
    onNavigateToKakaoLogin: () -> Unit,
    onNavigateToEmailLogin: () -> Unit,
) {
    val viewModel: OnboardingViewModel = koinViewModel()
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                OnboardingSideEffect.NavigateToKakaoLogin -> onNavigateToKakaoLogin()
                OnboardingSideEffect.NavigateToEmailLogin -> onNavigateToEmailLogin()
            }
        }
    }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800),
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .graphicsLayer { alpha = contentAlpha.value },
    ) {
        OnboardingContent(
            onKakaoClick = viewModel::onKakaoLoginClick,
            onEmailClick = viewModel::onEmailLoginClick,
        )
    }
}

@Composable
private fun OnboardingContent(
    onKakaoClick: () -> Unit,
    onEmailClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.statusBarsPadding())
        Spacer(Modifier.height(40.dp))

        Image(
            painter = painterResource(BugleIcon.AppLogo),
            contentDescription = "Bugle",
            modifier = Modifier.size(width = 153.dp, height = 84.dp),
        )

        Spacer(Modifier.height(10.dp))

        BasicText(
            text = "여행을 시작하고, 그 순간을 함께 공유해요.",
            style = BugleTypography.textM.copy(
                color = BugleColor.gray400,
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(Modifier.height(26.dp))

        OnboardingCardPager(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )

        BottomLoginSection(
            onKakaoClick = onKakaoClick,
            onEmailClick = onEmailClick,
        )
    }
}

@Composable
private fun OnboardingCardPager(modifier: Modifier = Modifier) {
    val actualPageCount = onboardingCards.size
    val startIndex = VIRTUAL_PAGE_COUNT / 2 - (VIRTUAL_PAGE_COUNT / 2 % actualPageCount)

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { VIRTUAL_PAGE_COUNT },
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(AUTO_SCROLL_DELAY)
            if (!pagerState.isScrollInProgress) {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1,
                    animationSpec = tween(durationMillis = 600),
                )
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 56.dp),
        pageSpacing = 16.dp,
    ) { page ->
        val actualPage = page % actualPageCount
        val pageOffset = ((pagerState.currentPage - page)
            + pagerState.currentPageOffsetFraction).absoluteValue

        val scale = 1f - (pageOffset * 0.1f).coerceIn(0f, 0.1f)
        val alpha = 1f - (pageOffset * 0.3f).coerceIn(0f, 0.3f)

        OnboardingCard(
            cardData = onboardingCards[actualPage],
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    alpha = alpha,
                ),
        )
    }
}

@Composable
private fun OnboardingCard(
    cardData: OnboardingCardData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(CardShape),
    ) {
        Image(
            painter = painterResource(cardData.image),
            contentDescription = cardData.label,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                    ),
                ),
        )

        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x4F1F1F1F),
                            Color(0x4F000000),
                        ),
                    ),
                    shape = RoundedCornerShape(32.dp),
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            BasicText(
                text = cardData.emoji,
                style = BugleTypography.sLabelL,
            )
            BasicText(
                text = cardData.label,
                style = BugleTypography.sLabelL.copy(color = Color.White),
            )
        }
    }
}

@Composable
private fun BottomLoginSection(
    onKakaoClick: () -> Unit,
    onEmailClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Black,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            )
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(32.dp))

        DividerWithText("회원가입/로그인")

        Spacer(Modifier.height(24.dp))

        BugleSocialButton(
            text = "카카오로 시작하기",
            onClick = onKakaoClick,
            icon = {
                Image(
                    painter = painterResource(BugleIcon.Kakao),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
            },
        )

        Spacer(Modifier.height(15.dp))

        BugleSocialButton(
            text = "이메일로 시작하기",
            onClick = onEmailClick,
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .border(1.dp, BugleColor.gray600, CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(BugleIcon.Email),
                        contentDescription = null,
                    )
                }
            },
        )

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun DividerWithText(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(BugleColor.gray600),
        )
        Spacer(Modifier.width(12.dp))
        BasicText(
            text = text,
            style = BugleTypography.textM.copy(
                color = BugleColor.gray600,
                textAlign = TextAlign.Center,
            ),
        )
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(BugleColor.gray600),
        )
    }
}