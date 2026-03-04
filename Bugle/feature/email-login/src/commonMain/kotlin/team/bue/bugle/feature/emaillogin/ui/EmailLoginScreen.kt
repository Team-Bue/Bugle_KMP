package team.bue.bugle.feature.emaillogin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
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
import team.bue.bugle.designsystem.util.clickable
import team.bue.bugle.feature.emaillogin.viewmodel.EmailLoginSideEffect
import team.bue.bugle.feature.emaillogin.viewmodel.EmailLoginUiState
import team.bue.bugle.feature.emaillogin.viewmodel.EmailLoginViewModel

@Composable
fun EmailLoginScreen(
    onBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToFindPassword: () -> Unit,
    onNavigateToFindId: () -> Unit,
) {
    val viewModel: EmailLoginViewModel = koinViewModel()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                EmailLoginSideEffect.NavigateToHome -> onNavigateToHome()
            }
        }
    }

    EmailLoginContent(
        uiState = viewModel.uiState.collectAsStateWithLifecycle().value,
        onBack = onBack,
        onLoginIdChange = viewModel::onLoginIdChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::onLoginClick,
        onNavigateToSignUp = onNavigateToSignUp,
        onFindPasswordClick = onNavigateToFindPassword,
        onFindIdClick = onNavigateToFindId,
    )
}

@Composable
private fun EmailLoginContent(
    uiState: EmailLoginUiState,
    onBack: () -> Unit,
    onLoginIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onFindPasswordClick: () -> Unit,
    onFindIdClick: () -> Unit,
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
                modifier = Modifier.clickable(
                    onClick = onBack,
                ),
                painter = painterResource(Res.drawable.ic_arrow),
                tint = BugleTheme.colors.outline,
                contentDescription = null,
            )

            Spacer(Modifier.height(12.dp))

            BasicText(
                text = "로그인",
                style = BugleTypography.titleL.copy(color = BugleColor.white),
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                BugleTextField(
                    value = uiState.loginId,
                    onValueChange = onLoginIdChange,
                    label = "이메일 및 아이디",
                    placeholder = "이메일 및 아이디",
                    showClearIcon = true,
                    isError = uiState.loginIdError != null,
                    errorMessage = uiState.loginIdError ?: "",
                )

                BugleTextField(
                    value = uiState.password,
                    onValueChange = onPasswordChange,
                    label = "비밀번호",
                    placeholder = "비밀번호",
                    showVisibleIcon = true,
                    showClearIcon = true,
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError ?: "",
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicText(
                    text = "비밀번호 찾기",
                    style = BugleTypography.textL.copy(color = BugleColor.gray500),
                    modifier = Modifier.clickable(onClick = onFindPasswordClick),
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .width(1.dp)
                        .background(BugleColor.gray500),
                )
                Spacer(Modifier.width(8.dp))
                BasicText(
                    text = "아이디 찾기",
                    style = BugleTypography.textL.copy(color = BugleColor.gray500),
                    modifier = Modifier.clickable(onClick = onFindIdClick),
                )
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicText(
                    text = "계정이 없으신가요?",
                    style = BugleTypography.textL.copy(color = BugleColor.gray700),
                )
                Spacer(Modifier.width(4.dp))
                BasicText(
                    text = "회원가입",
                    style = BugleTypography.textL.copy(
                        color = BugleColor.primary500,
                        textDecoration = TextDecoration.Underline,
                    ),
                    modifier = Modifier.clickable(onClick = onNavigateToSignUp),
                )
            }

            BugleButton(
                text = if (uiState.isLoading) "로그인 중..." else "로그인",
                onClick = onLoginClick,
                enabled = uiState.isLoginEnabled,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
