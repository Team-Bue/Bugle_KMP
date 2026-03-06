package team.bue.bugle.feature.findpassword.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import team.bue.bugle.feature.findpassword.viewmodel.FindPasswordSideEffect
import team.bue.bugle.feature.findpassword.viewmodel.FindPasswordStep
import team.bue.bugle.feature.findpassword.viewmodel.FindPasswordUiState
import team.bue.bugle.feature.findpassword.viewmodel.FindPasswordViewModel

@Composable
fun FindPasswordScreen(
    onExit: () -> Unit,
) {
    val viewModel: FindPasswordViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                FindPasswordSideEffect.Exit -> onExit()
            }
        }
    }

    FindPasswordContent(
        uiState = uiState,
        onBack = viewModel::onBack,
        onEmailChange = viewModel::onEmailChange,
        onCodeChange = viewModel::onCodeChange,
        onPasswordChange = viewModel::onPasswordChange,
        onPasswordConfirmChange = viewModel::onPasswordConfirmChange,
        onSendCode = viewModel::onSendCode,
        onVerifyCode = viewModel::onVerifyCode,
        onResetPassword = viewModel::onResetPassword,
    )
}

@Composable
private fun FindPasswordContent(
    uiState: FindPasswordUiState,
    onBack: () -> Unit,
    onEmailChange: (String) -> Unit,
    onCodeChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordConfirmChange: (String) -> Unit,
    onSendCode: () -> Unit,
    onVerifyCode: () -> Unit,
    onResetPassword: () -> Unit,
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
                    FindPasswordStep.ENTER_EMAIL -> "사용자님의\n이메일을 알려주세요!"
                    FindPasswordStep.VERIFY_CODE -> "인증코드를\n입력해주세요!"
                    FindPasswordStep.RESET_PASSWORD -> "비밀번호를\n재설정해주세요!"
                }
            val subtitle =
                when (uiState.step) {
                    FindPasswordStep.ENTER_EMAIL -> "인증 코드를 받을 이메일을 작성해주세요."
                    FindPasswordStep.VERIFY_CODE -> "작성하신 이메일로 인증코드가 전송되었어요!"
                    FindPasswordStep.RESET_PASSWORD -> "다른 사람이 유추하지 못하는 비밀번호로 설정해주세요!"
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
                FindPasswordStep.ENTER_EMAIL -> {
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

                FindPasswordStep.VERIFY_CODE -> {
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

                FindPasswordStep.RESET_PASSWORD -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        BugleTextField(
                            value = uiState.password,
                            onValueChange = onPasswordChange,
                            label = "비밀번호",
                            placeholder = "비밀번호",
                            showVisibleIcon = true,
                            showClearIcon = true,
                            isError = uiState.passwordError != null,
                            errorMessage = uiState.passwordError ?: (uiState.submitError ?: ""),
                        )

                        BugleTextField(
                            value = uiState.passwordConfirm,
                            onValueChange = onPasswordConfirmChange,
                            label = "비밀번호 재입력",
                            placeholder = "비밀번호를 다시 입력",
                            showVisibleIcon = true,
                            showClearIcon = true,
                            isError = uiState.passwordError != null,
                            errorMessage = "",
                        )
                    }
                }
            }

            if (uiState.step != FindPasswordStep.VERIFY_CODE) {
                Spacer(Modifier.weight(1f))
            }

            if (uiState.step == FindPasswordStep.VERIFY_CODE) {
                Spacer(Modifier.height(24.dp))
            }

            val buttonText =
                when (uiState.step) {
                    FindPasswordStep.ENTER_EMAIL -> "다음"
                    FindPasswordStep.VERIFY_CODE -> "다음"
                    FindPasswordStep.RESET_PASSWORD -> "완료"
                }
            val buttonEnabled =
                when (uiState.step) {
                    FindPasswordStep.ENTER_EMAIL -> uiState.email.isNotBlank() && !uiState.isLoading
                    FindPasswordStep.VERIFY_CODE -> uiState.code.length == 6 && !uiState.isLoading
                    FindPasswordStep.RESET_PASSWORD -> uiState.password.isNotBlank() && uiState.passwordConfirm.isNotBlank() && !uiState.isLoading
                }
            val action =
                when (uiState.step) {
                    FindPasswordStep.ENTER_EMAIL -> onSendCode
                    FindPasswordStep.VERIFY_CODE -> onVerifyCode
                    FindPasswordStep.RESET_PASSWORD -> onResetPassword
                }

            BugleButton(
                text = buttonText,
                onClick = action,
                enabled = buttonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
            )
        }
    }
}
