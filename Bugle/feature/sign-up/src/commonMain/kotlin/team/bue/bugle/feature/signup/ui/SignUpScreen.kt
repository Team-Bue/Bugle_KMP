package team.bue.bugle.feature.signup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
                onAgreeService = viewModel::onAgreeServiceTerm,
                onAgreePrivacy = viewModel::onAgreePrivacyTerm,
                onAgreeMarketing = viewModel::onAgreeMarketingTerm,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
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

            TermsHeaderImage()

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

            Spacer(Modifier.height(24.dp))
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
    onAgreeService: () -> Unit,
    onAgreePrivacy: () -> Unit,
    onAgreeMarketing: () -> Unit,
    onNext: () -> Unit,
) {
    var detailType by remember { mutableStateOf<TermDetailType?>(null) }
    val isDetailMode = detailType != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BugleColor.black),
    ) {
        TermsHeaderImage(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 184.dp),
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .then(
                    if (isDetailMode) {
                        Modifier.fillMaxHeight(0.9f)
                    } else {
                        Modifier.height(435.dp)
                    },
                )
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color(0xFF121212)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 40.dp),
            ) {
                if (isDetailMode) {
                    TermDetailScreen(
                        type = detailType!!,
                        checked = when (detailType) {
                            TermDetailType.SERVICE -> uiState.isAgreedServiceTerm
                            TermDetailType.PRIVACY -> uiState.isAgreedPrivacyTerm
                            TermDetailType.MARKETING -> uiState.isAgreedMarketingTerm
                            else -> false
                        },
                        onBack = { detailType = null },
                        onToggle = when (detailType) {
                            TermDetailType.SERVICE -> onToggleService
                            TermDetailType.PRIVACY -> onTogglePrivacy
                            TermDetailType.MARKETING -> onToggleMarketing
                            else -> ({})
                        },
                        onAgree = {
                            when (detailType) {
                                TermDetailType.SERVICE -> onAgreeService()
                                TermDetailType.PRIVACY -> onAgreePrivacy()
                                TermDetailType.MARKETING -> onAgreeMarketing()
                                null -> Unit
                            }
                            detailType = null
                        },
                    )
                } else {
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
                        onOpenDetail = { detailType = TermDetailType.SERVICE },
                        onToggle = onToggleService,
                    )

                    Spacer(Modifier.height(20.dp))

                    AgreementTrailingRow(
                        text = "개인정보 수집·이용 동의 (필수)",
                        checked = uiState.isAgreedPrivacyTerm,
                        onOpenDetail = { detailType = TermDetailType.PRIVACY },
                        onToggle = onTogglePrivacy,
                    )

                    Spacer(Modifier.height(20.dp))

                    AgreementTrailingRow(
                        text = "마케팅 정보 수신 동의 (선택)",
                        checked = uiState.isAgreedMarketingTerm,
                        onOpenDetail = { detailType = TermDetailType.MARKETING },
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun TermsHeaderImage(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(BugleIcon.ThreeDLogo),
        contentDescription = null,
        modifier = modifier.size(247.dp),
    )
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
    onOpenDetail: () -> Unit,
    onToggle: () -> Unit,
) {
    val checkedColor = Color(0xFFFF4FB6)

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = text,
            style = BugleTypography.textM.copy(color = if (checked) checkedColor else BugleColor.gray500),
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onOpenDetail),
        )

        Icon(
            painter = painterResource(BugleIcon.Check),
            contentDescription = null,
            tint = if (checked) checkedColor else BugleColor.gray600,
            modifier = Modifier
                .size(20.dp)
                .clickable(onClick = onOpenDetail),
        )

        Spacer(Modifier.width(12.dp))
    }
}

private enum class TermDetailType {
    SERVICE,
    PRIVACY,
    MARKETING,
}

@Composable
private fun TermDetailScreen(
    type: TermDetailType,
    checked: Boolean,
    onBack: () -> Unit,
    onToggle: () -> Unit,
    onAgree: () -> Unit,
) {
    val detail = remember(type) {
        when (type) {
            TermDetailType.SERVICE -> TermDetailContent(
                body = """
                    본 이용약관(이하 "약관")은 bue(이하 "회사")가 제공하는 여행 사진 공유 SNS 서비스 bugle(이하 "서비스")의 이용과 관련하여 회사와 이용자 간의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.
                    이용자는 본 약관에 동의함으로써 서비스 이용계약이 체결됩니다.

                    제1조 (목적)
                    본 약관은 회사가 제공하는 여행 사진 공유 SNS 서비스 bugle의 이용조건 및 절차, 회사와 이용자의 권리·의무 및 책임사항을 규정함을 목적으로 합니다.

                    제2조 (약관의 효력 및 변경)
                    본 약관은 서비스 화면에 게시함으로써 효력이 발생합니다.
                    회사는 관련 법령을 위반하지 않는 범위에서 본 약관을 변경할 수 있습니다.
                    약관 변경 시 적용일자 및 변경사유를 사전에 공지합니다.
                    이용자가 변경 약관에 동의하지 않을 경우 서비스 이용을 중단하고 탈퇴할 수 있습니다.

                    제3조 (회원가입 및 계정 관리)
                    이용자는 회사가 정한 절차에 따라 회원가입을 신청하고 약관에 동의함으로써 회원이 됩니다.
                    이용자는 정확하고 최신의 정보를 제공해야 합니다.
                    허위 정보 제공으로 인한 책임은 이용자에게 있습니다.
                    계정 관리 책임은 이용자 본인에게 있으며, 타인에게 양도·대여할 수 없습니다.
                    계정 도용이 의심되는 경우 즉시 회사에 통지해야 합니다.

                    제4조 (서비스의 내용)
                    회사는 다음과 같은 서비스를 제공합니다.
                    여행 사진 업로드 및 공유 기능
                    게시물 열람 및 상호 소통 기능 (댓글, 좋아요 등)
                    여행 관련 콘텐츠 탐색 및 추천 기능
                    기타 회사가 정하는 부가 서비스
                    회사는 운영상 또는 기술상의 필요에 따라 서비스의 일부 또는 전부를 변경하거나 중단할 수 있습니다.

                    제5조 (게시물의 저작권 및 이용 허락)
                    이용자가 서비스에 게시한 사진 및 콘텐츠(이하 "게시물")의 저작권은 해당 이용자에게 귀속됩니다.
                    이용자는 게시물을 서비스 운영, 홍보, 마케팅, 서비스 개선을 위하여 비독점적으로 사용할 수 있는 권리를 회사에 부여합니다.
                    회사는 서비스 운영 목적 범위를 벗어나 게시물을 상업적으로 이용하지 않습니다.

                    제6조 (이용자의 금지행위)
                    이용자는 다음 행위를 하여서는 안 됩니다.
                    타인의 사진·저작물 무단 업로드
                    음란물, 불법 촬영물, 혐오·차별 콘텐츠 게시
                    타인의 권리(초상권·저작권 등)를 침해하는 행위
                    허위 정보 또는 광고성 도배 게시
                    서비스 운영을 방해하는 행위
                    위 행위 발생 시 회사는 사전 통지 없이 게시물 삭제, 이용 제한 또는 계정 해지 조치를 할 수 있습니다.

                    제7조 (게시물 관리)
                    회사는 관련 법령에 위반되거나 약관을 위반한 게시물을 삭제할 수 있습니다.
                    이용자는 언제든지 본인이 작성한 게시물을 삭제할 수 있습니다.

                    제8조 (책임의 제한)
                    회사는 이용자가 게시한 정보의 정확성, 신뢰성, 적법성에 대해 보증하지 않습니다.
                    이용자 간 분쟁에 대해서는 회사는 개입하지 않으며 책임을 지지 않습니다.
                    천재지변, 시스템 장애 등 불가항력적 사유로 인한 서비스 중단에 대해 회사는 책임을 지지 않습니다.

                    제9조 (계약 해지)
                    이용자는 언제든지 회원 탈퇴를 요청할 수 있습니다.
                    이용자가 약관을 위반한 경우 회사는 이용계약을 해지할 수 있습니다.

                    제10조 (준거법 및 관할)
                    본 약관은 대한민국 법령에 따라 해석되며, 서비스 이용과 관련한 분쟁은 대한민국 법원을 관할 법원으로 합니다.

                    부칙
                    본 약관은 2026년 3월 2일부터 시행합니다.
                """.trimIndent(),
            )

            TermDetailType.PRIVACY -> TermDetailContent(
                body = """
                    bue(이하 "회사")는 bugle 서비스 제공을 위하여 「개인정보 보호법」에 따라 아래와 같이 개인정보를 수집·이용합니다.
                    이용자는 아래 내용을 충분히 확인한 후 동의 여부를 결정할 수 있으며, 동의하지 않을 경우 회원가입 및 서비스 이용이 제한될 수 있습니다.

                    1. 수집하는 개인정보 항목
                    (1) 회원가입 시
                    이메일 주소
                    비밀번호
                    닉네임
                    프로필 이미지(선택 입력 시)

                    (2) 서비스 이용 과정에서 자동 수집
                    접속 IP 주소
                    기기 정보(OS, 브라우저 정보 등)
                    서비스 이용 기록
                    쿠키 및 로그 데이터

                    (3) 게시물 업로드 시
                    업로드한 사진
                    위치 정보(이용자가 직접 입력하거나 동의한 경우)
                    게시글 내용

                    2. 개인정보 수집·이용 목적
                    회사는 다음 목적을 위해 개인정보를 수집·이용합니다.
                    회원 식별 및 본인 확인
                    서비스 제공 및 운영
                    여행 사진 콘텐츠 공유 및 추천 기능 제공
                    부정 이용 방지 및 서비스 안정성 확보
                    문의 응대 및 고객 지원
                    법령 및 약관 위반 행위 대응

                    3. 개인정보 보유 및 이용 기간
                    회사는 원칙적으로 회원 탈퇴 시까지 개인정보를 보유·이용합니다.
                    단, 다음의 경우에는 관련 법령에 따라 일정 기간 보관합니다.
                    계약 또는 청약철회 기록: 5년
                    대금 결제 및 재화 공급 기록: 5년
                    소비자 불만 또는 분쟁 처리 기록: 3년
                    로그 기록: 3개월
                    (전자상거래 등에서의 소비자보호에 관한 법률 등 관련 법령에 따름)

                    4. 동의 거부 권리 및 불이익
                    이용자는 개인정보 수집·이용에 대한 동의를 거부할 권리가 있습니다.
                    다만, 필수 항목에 대한 동의를 거부할 경우 서비스 이용이 제한될 수 있습니다.
                """.trimIndent(),
            )

            TermDetailType.MARKETING -> TermDetailContent(
                body = """
                    bue(이하 "회사")는 bugle 서비스와 관련된 이벤트, 혜택, 신규 기능 안내 등 마케팅 정보를 이용자에게 제공하기 위하여 아래와 같이 개인정보를 수집·이용합니다.
                    이 동의는 선택사항이며, 동의하지 않아도 서비스 이용에는 제한이 없습니다.

                    1. 수집·이용 항목
                    이메일 주소
                    휴대 전화번호
                    서비스 이용 기록(관심 콘텐츠, 활동 내역 등)

                    2. 이용 목적
                    신규 서비스 및 기능 안내
                    여행 관련 이벤트 및 프로모션 정보 제공
                    맞춤형 콘텐츠 및 혜택 안내
                    제휴 이벤트 및 캠페인 정보 안내

                    3. 보유 및 이용 기간
                    회원 탈퇴 또는 마케팅 수신 동의 철회 시까지 보유·이용합니다.

                    4. 전송 방법
                    회사는 다음의 방법으로 마케팅 정보를 발송할 수 있습니다.
                    이메일
                    앱 푸시 알림
                    문자메시지(SMS)
                    앱 내 알림

                    5. 동의 철회
                    이용자는 언제든지 다음 방법을 통해 마케팅 정보 수신 동의를 철회할 수 있습니다.
                    앱 설정 내 수신 동의 해제
                    수신 메시지 내 수신거부 링크 클릭
                    고객센터 문의
                    철회 시 지체 없이 마케팅 정보 발송을 중단합니다.
                """.trimIndent(),
            )
        }
    }

    val termsBodyScrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        Icon(
            painter = painterResource(BugleIcon.Arrow),
            contentDescription = null,
            tint = BugleTheme.colors.outline,
            modifier = Modifier.clickable(onClick = onBack),
        )

        Spacer(Modifier.height(16.dp))

        BasicText(
            text = when (type) {
                TermDetailType.SERVICE -> "서비스 이용 약관 동의 (필수)"
                TermDetailType.PRIVACY -> "개인정보 수집·이용 동의 (필수)"
                TermDetailType.MARKETING -> "마케팅 정보 수신 동의 (선택)"
            },
            style = BugleTypography.sTitleM.copy(color = BugleColor.white),
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            BasicText(
                text = detail.body,
                style = BugleTypography.textS.copy(color = BugleColor.gray400),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(termsBodyScrollState),
            )
        }

        Spacer(Modifier.height(24.dp))

        ConsentRow(
            checked = checked,
            onToggle = onToggle,
        )

        Spacer(Modifier.height(24.dp))

        BugleButton(
            text = "동의",
            onClick = onAgree,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))
    }
}

private data class TermDetailContent(
    val body: String,
)

@Composable
private fun ConsentRow(
    checked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicText(
            text = "약관에 동의하십니까?",
            style = BugleTypography.textM.copy(color = BugleColor.white),
        )

        Spacer(Modifier.width(12.dp))

        AgreementCircleIndicator(checked = checked)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            )
        }
    }
}
