package team.bue.bugle.feature.findid.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bugle.core.design_system.generated.resources.Res
import bugle.core.design_system.generated.resources.ic_arrow
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import team.bue.bugle.designsystem.button.BugleButton
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.textfield.BugleTextField
import team.bue.bugle.designsystem.textfield.BugleVerificationCodeField
import team.bue.bugle.designsystem.util.clickable
import team.bue.bugle.feature.findid.viewmodel.FindIdSideEffect
import team.bue.bugle.feature.findid.viewmodel.FindIdStep
import team.bue.bugle.feature.findid.viewmodel.FindIdUiState
import team.bue.bugle.feature.findid.viewmodel.FindIdViewModel

@Composable
fun FindIdScreen(
    onExit: () -> Unit,
) {
    val viewModel: FindIdViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                FindIdSideEffect.Exit -> onExit()
            }
        }
    }

    FindIdContent(
        uiState = uiState,
        onBack = viewModel::onBack,
        onEmailChange = viewModel::onEmailChange,
        onCodeChange = viewModel::onCodeChange,
        onSendCode = viewModel::onSendCode,
        onVerifyCode = viewModel::onVerifyCode,
        onComplete = viewModel::onComplete,
    )
}

@Composable
private fun FindIdContent(
    uiState: FindIdUiState,
    onBack: () -> Unit,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onVerifyCode: () -> Unit,
    onComplete: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BugleColor.black),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow),
                contentDescription = null,
                tint = BugleTheme.colors.outline,
                modifier = Modifier.clickable(onClick = onBack),
            )

            Spacer(Modifier.height(12.dp))

            val title =
                when (uiState.step) {
                    FindIdStep.ENTER_EMAIL -> "사용자님의\n이메일을 알려주세요!"
                    FindIdStep.VERIFY_CODE -> "인증코드를\n입력해주세요!"
                    FindIdStep.RESULT -> "회원님의 아이디를 찾았어요!"
                }
            val subtitle =
                when (uiState.step) {
                    FindIdStep.ENTER_EMAIL -> "인증 코드를 받을 이메일을 작성해주세요."
                    FindIdStep.VERIFY_CODE -> "작성하신 이메일로 인증코드가 전송되었어요!"
                    FindIdStep.RESULT -> "아래 아이디가 회원님의 아이디 입니다!"
                }

            BasicText(
                text = title,
                style = BugleTypography.titleL.copy(color = BugleColor.white),
            )

            Spacer(Modifier.height(8.dp))

            BasicText(
                text = subtitle,
                style = BugleTypography.textS.copy(color = BugleColor.gray500),
            )

            Spacer(Modifier.height(24.dp))

            when (uiState.step) {
                FindIdStep.ENTER_EMAIL -> {
                    BugleTextField(
                        value = uiState.email,
                        onValueChange = onEmailChange,
                        label = "이메일",
                        placeholder = "이메일",
                        showClearIcon = true,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError ?: "",
                    )
                }

                FindIdStep.VERIFY_CODE -> {
                    BugleVerificationCodeField(
                        code = uiState.code,
                        onCodeChange = onCodeChange,
                        onResendClick = onSendCode,
                        modifier = Modifier.weight(1f),
                    )
                    if (uiState.codeError != null) {
                        Spacer(Modifier.height(12.dp))

                        BasicText(
                            text = uiState.codeError,
                            style = BugleTypography.textM.copy(color = BugleTheme.colors.error),
                        )
                    }

                }

                FindIdStep.RESULT -> {
                    var showResult by remember(uiState.foundAccountId) { mutableStateOf(false) }
                    LaunchedEffect(uiState.foundAccountId) {
                        showResult = uiState.foundAccountId != null
                    }

                    AnimatedVisibility(
                        visible = showResult,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(BugleColor.gray900)
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                BasicText(
                                    text = "아이디",
                                    style = BugleTypography.textL.copy(color = BugleColor.gray500),
                                )
                                BasicText(
                                    text = uiState.foundAccountId.orEmpty(),
                                    style = BugleTypography.labelM.copy(color = BugleColor.primary500),
                                )
                            }
                        }
                    }
                }
            }

            if (uiState.step != FindIdStep.VERIFY_CODE) {
                Spacer(Modifier.weight(1f))
            }

            if (uiState.step == FindIdStep.VERIFY_CODE) {
                Spacer(Modifier.height(24.dp))
            }

            val buttonText = if (uiState.step == FindIdStep.RESULT) "완료" else "다음"
            val buttonEnabled =
                when (uiState.step) {
                    FindIdStep.ENTER_EMAIL -> uiState.email.isNotBlank() && !uiState.isLoading
                    FindIdStep.VERIFY_CODE -> uiState.code.length == 6 && !uiState.isLoading
                    FindIdStep.RESULT -> true
                }
            val action =
                when (uiState.step) {
                    FindIdStep.ENTER_EMAIL -> onSendCode
                    FindIdStep.VERIFY_CODE -> onVerifyCode
                    FindIdStep.RESULT -> onComplete
                }

            BugleButton(
                text = buttonText,
                onClick = action,
                enabled = buttonEnabled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

