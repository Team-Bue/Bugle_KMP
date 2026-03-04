package team.bue.bugle.feature.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import team.bue.bugle.designsystem.button.BugleButton
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleIcon
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.textfield.BugleTextField
import team.bue.bugle.designsystem.textfield.BugleVerificationCodeField
import team.bue.bugle.designsystem.util.clickable
import team.bue.bugle.feature.signup.viewmodel.SignUpSideEffect
import team.bue.bugle.feature.signup.viewmodel.SignUpStep
import team.bue.bugle.feature.signup.viewmodel.SignUpUiState
import team.bue.bugle.feature.signup.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val viewModel: SignUpViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                SignUpSideEffect.NavigateBack -> onBack()
                SignUpSideEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    SignUpBackHandlerEffect(
        enabled = true,
        onBack = viewModel::onSystemBack,
    )

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState.step) {
            SignUpStep.ENTER_EMAIL -> EnterEmailStep(
                uiState = uiState,
                onBack = viewModel::onBack,
                onEmailChange = viewModel::onEmailChange,
                onSendCode = viewModel::onSendCode,
            )
            SignUpStep.VERIFY_EMAIL -> VerifyEmailStep(
                uiState = uiState,
                onBack = viewModel::onBack,
                onVerificationCodeChange = viewModel::onVerificationCodeChange,
                onVerifyCode = viewModel::onVerifyCode,
                onResendCode = viewModel::onSendCode,
            )
            SignUpStep.SET_PASSWORD -> SetPasswordStep(
                uiState = uiState,
                onBack = viewModel::onBack,
                onPasswordChange = viewModel::onPasswordChange,
                onPasswordConfirmChange = viewModel::onPasswordConfirmChange,
                onNext = viewModel::onNextFromPassword,
            )
            SignUpStep.AGREE_TERMS -> TermsAgreementStep(
                uiState = uiState,
                onBack = viewModel::onBack,
                onToggleAll = viewModel::onToggleAllTerms,
                onToggleService = viewModel::onToggleServiceTerm,
                onTogglePrivacy = viewModel::onTogglePrivacyTerm,
                onToggleMarketing = viewModel::onToggleMarketingTerm,
                onNext = viewModel::onSignUp,
            )
            SignUpStep.SET_ACCOUNT_ID -> SetAccountIdStep(
                uiState = uiState,
                onBack = viewModel::onBack,
                onAccountIdChange = viewModel::onAccountIdChange,
                onNext = viewModel::onNextFromAccountId,
            )
            SignUpStep.SET_USERNAME -> SetUserNameStep(
                uiState = uiState,
                onBack = viewModel::onBack,
                onUserNameChange = viewModel::onUserNameChange,
                onNext = viewModel::onNextFromUserName,
            )
            SignUpStep.COMPLETE -> CompleteStep(
                onCompleteClick = viewModel::onCompleteClick,
            )
        }

        if (uiState.showExitDialog) {
            SignUpExitDialog(
                onDismiss = viewModel::onExitDialogDismiss,
                onConfirm = viewModel::onExitDialogConfirm,
            )
        }
    }
}

@Composable
private fun EnterEmailStep(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onEmailChange: (String) -> Unit,
    onSendCode: () -> Unit,
) {
    SignUpStepLayout(
        onBack = onBack,
        title = "사용자님의\n이메일을 알려주세요!",
        subtitle = "인증 코드를 받을 이메일을 작성해주세요.",
        buttonText = if (uiState.isLoading) "발송 중..." else "인증코드 발송",
        buttonEnabled = !uiState.isLoading, // TEMP: UI 확인용 바이패스
        onButtonClick = onSendCode,
    ) {
        BugleTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = "이메일",
            placeholder = "이메일을 입력해주세요.",
            showClearIcon = true,
            isError = uiState.emailError != null,
            errorMessage = uiState.emailError ?: "",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun VerifyEmailStep(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onVerificationCodeChange: (String) -> Unit,
    onVerifyCode: () -> Unit,
    onResendCode: () -> Unit,
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
                painter = painterResource(BugleIcon.Arrow),
                contentDescription = null,
                tint = BugleTheme.colors.outline,
                modifier = Modifier.clickable(onClick = onBack),
            )

            Spacer(Modifier.height(12.dp))

            BasicText(
                text = "인증코드를\n입력해주세요!",
                style = BugleTypography.titleL.copy(color = BugleColor.white),
            )

            Spacer(Modifier.height(8.dp))

            BasicText(
                text = "작성하신 이메일로 인증코드가 전송되었어요!",
                style = BugleTypography.textS.copy(color = BugleColor.gray500),
            )

            Spacer(Modifier.height(24.dp))

            BugleVerificationCodeField(
                code = uiState.verificationCode,
                onCodeChange = onVerificationCodeChange,
                onResendClick = onResendCode,
                modifier = Modifier.weight(1f),
            )

            if (uiState.codeError != null) {
                Spacer(Modifier.height(12.dp))
                BasicText(
                    text = uiState.codeError,
                    style = BugleTypography.textM.copy(color = BugleTheme.colors.error),
                )
            }

            Spacer(Modifier.height(24.dp))

            BugleButton(
                text = if (uiState.isLoading) "확인 중..." else "다음",
                onClick = onVerifyCode,
                enabled = !uiState.isLoading && uiState.verificationCode.length == 6,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SetPasswordStep(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    SignUpStepLayout(
        onBack = onBack,
        title = "비밀번호를\n설정해주세요!",
        subtitle = "다른 사람이 유추하지 못하는 비밀번호로 설정해주세요!",
        buttonText = if (uiState.isLoading) "가입 중..." else "다음",
        buttonEnabled = !uiState.isLoading,
        onButtonClick = onNext,
        errorMessage = uiState.passwordError ?: uiState.signUpError,
    ) {
        SignUpUnderlineInput(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = "비밀번호",
            placeholder = "비밀번호",
            isPassword = true,
        )

        Spacer(Modifier.height(24.dp))

        SignUpUnderlineInput(
            value = uiState.passwordConfirm,
            onValueChange = onPasswordConfirmChange,
            label = "비밀번호 재입력",
            placeholder = "비밀번호를 다시 입력",
            isPassword = true,
        )
    }
}

@Composable
private fun SetAccountIdStep(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onAccountIdChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    SignUpStepLayout(
        onBack = onBack,
        title = "사용자님의\n아이디를 설정해주세요!",
        subtitle = "다른 사용자들에게 보일 아이디를 설정해주세요!",
        buttonText = "다음",
        buttonEnabled = uiState.accountId.isNotBlank() && !uiState.isLoading,
        onButtonClick = onNext,
        errorMessage = uiState.accountIdError,
    ) {
        SignUpUnderlineInput(
            value = uiState.accountId,
            onValueChange = onAccountIdChange,
            label = "아이디",
            placeholder = "아이디",
        )
    }
}

@Composable
private fun SetUserNameStep(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onUserNameChange: (String) -> Unit,
    onNext: () -> Unit,
) {
    SignUpStepLayout(
        onBack = onBack,
        title = "사용자님의\n이름을 설정해주세요!",
        subtitle = "다른 사용자들에게 보일 이름을 설정해주세요!",
        buttonText = "다음",
        buttonEnabled = uiState.userName.isNotBlank() && !uiState.isLoading,
        onButtonClick = onNext,
        errorMessage = uiState.userNameError,
    ) {
        SignUpUnderlineInput(
            value = uiState.userName,
            onValueChange = onUserNameChange,
            label = "이름",
            placeholder = "이름",
        )
    }
}

@Composable
private fun SignUpUnderlineInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BasicText(
            text = label,
            style = BugleTypography.textL.copy(color = BugleColor.white),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 12.dp, top = 18.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                textStyle = BugleTypography.sLabelM.copy(color = BugleColor.white),
                modifier = Modifier.fillMaxWidth(),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        BasicText(
                            text = placeholder,
                            style = BugleTypography.sLabelM.copy(color = BugleColor.gray700),
                        )
                    }
                    innerTextField()
                },
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(BugleColor.primary500),
        )
    }
}

@Composable
private fun CompleteStep(
    onCompleteClick: () -> Unit,
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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(184.dp))

            GlowStarPlaceholder()

            Spacer(Modifier.height(188.dp))

            BasicText(
                text = "함께 여행을\n떠나고, 공유해볼까요?",
                style = BugleTypography.titleL.copy(
                    color = BugleColor.white,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(Modifier.height(12.dp))

            BasicText(
                text = "여행을 시작하고, 그 순간을 함께 공유해요.",
                style = BugleTypography.textS.copy(
                    color = BugleColor.gray500,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(Modifier.weight(1f))

            BugleButton(
                text = "시작하기",
                onClick = onCompleteClick,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(56.dp))
        }
    }
}

@Composable
private fun TermsAgreementStep(
    uiState: SignUpUiState,
    onBack: () -> Unit,
    onToggleAll: () -> Unit,
    onToggleService: () -> Unit,
    onTogglePrivacy: () -> Unit,
    onToggleMarketing: () -> Unit,
    onNext: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BugleColor.black),
    ) {
        GlowStarPlaceholder(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 184.dp),
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(435.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFF121212)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
            ) {
                BasicText(
                    text = "시작에 앞서,\n약관에 동의를 해주세요!",
                    style = BugleTypography.sTitleM.copy(color = BugleColor.white),
                )

                Spacer(Modifier.height(20.dp))

                AllAgreementRow(
                    checked = uiState.isAllTermsAgreed,
                    onToggle = onToggleAll,
                )

                Spacer(Modifier.height(20.dp))

                AgreementTrailingRow(
                    text = "서비스 이용 약관 동의 (필수)",
                    checked = uiState.isAgreedServiceTerm,
                    onToggle = onToggleService,
                )

                Spacer(Modifier.height(20.dp))

                AgreementTrailingRow(
                    text = "개인정보 수집·이용 동의 (필수)",
                    checked = uiState.isAgreedPrivacyTerm,
                    onToggle = onTogglePrivacy,
                )

                Spacer(Modifier.height(20.dp))

                AgreementTrailingRow(
                    text = "마케팅 정보 수신 동의 (선택)",
                    checked = uiState.isAgreedMarketingTerm,
                    onToggle = onToggleMarketing,
                )

                Spacer(Modifier.weight(1f))

                if (uiState.signUpError != null) {
                    BasicText(
                        text = uiState.signUpError,
                        style = BugleTypography.textM.copy(color = BugleTheme.colors.error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                    )
                }

                BugleButton(
                    text = if (uiState.isLoading) "처리 중..." else "완료",
                    onClick = onNext,
                    enabled = !uiState.isLoading && uiState.isRequiredTermsAgreed,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun GlowStarPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(247.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(247.dp)
                .clip(CircleShape)
                .background(Color(0x22050014)),
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color(0x331B001E)),
        )

        BasicText(
            text = "✶",
            style = BugleTypography.titleL.copy(
                color = Color(0xFFFF62C7),
                fontSize = 156.sp,
                shadow = Shadow(
                    color = Color(0xCCFF4FB6),
                    offset = Offset(0f, 0f),
                    blurRadius = 36f,
                ),
            ),
        )
    }
}

@Composable
private fun AllAgreementRow(
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(47.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF262626))
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = "약관 전체 동의",
            style = BugleTypography.sLabelL.copy(color = BugleColor.white),
        )

        Spacer(Modifier.weight(1f))

        AgreementCircleIndicator(checked = checked)
    }
}

@Composable
private fun AgreementTrailingRow(
    text: String,
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = text,
            style = BugleTypography.textM.copy(color = if (checked) BugleColor.white else BugleColor.gray500),
        )

        Spacer(Modifier.weight(1f))

        BasicText(
            text = ">",
            style = BugleTypography.sLabelL.copy(color = BugleColor.gray600),
        )
    }
}

@Composable
private fun AgreementCircleIndicator(
    checked: Boolean,
) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (checked) BugleColor.primary500 else BugleColor.gray600,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(BugleColor.primary500),
            )
        }
    }
}

@Composable
private fun SignUpExitDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(BugleColor.gray900)
                .padding(24.dp),
        ) {
            BasicText(
                text = "회원가입을 중단할까요?",
                style = BugleTypography.sTitleM.copy(color = BugleColor.white),
            )

            Spacer(Modifier.height(8.dp))

            BasicText(
                text = "지금 나가면 입력한 정보가 저장되지 않아요.",
                style = BugleTypography.textM.copy(color = BugleColor.gray400),
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BugleColor.gray700)
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = "계속하기",
                        style = BugleTypography.sLabelL.copy(color = BugleColor.white),
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BugleColor.primary500)
                        .clickable(onClick = onConfirm)
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = "나가기",
                        style = BugleTypography.sLabelL.copy(color = BugleColor.white),
                    )
                }
            }
        }
    }
}

@Composable
private fun SignUpStepLayout(
    onBack: () -> Unit,
    title: String,
    subtitle: String,
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    errorMessage: String? = null,
    content: @Composable () -> Unit,
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
                painter = painterResource(BugleIcon.Arrow),
                contentDescription = null,
                tint = BugleTheme.colors.outline,
                modifier = Modifier.clickable(onClick = onBack),
            )

            Spacer(Modifier.height(12.dp))

            BasicText(
                text = title,
                style = BugleTypography.titleL.copy(color = BugleColor.white),
            )

            Spacer(Modifier.height(8.dp))

            BasicText(
                text = subtitle,
                style = BugleTypography.textM.copy(color = BugleColor.gray500),
            )

            Spacer(Modifier.height(24.dp))

            content()

            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                BasicText(
                    text = errorMessage,
                    style = BugleTypography.textM.copy(color = BugleTheme.colors.error),
                )
            }

            Spacer(Modifier.weight(1f))

            BugleButton(
                text = buttonText,
                onClick = onButtonClick,
                enabled = buttonEnabled,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
