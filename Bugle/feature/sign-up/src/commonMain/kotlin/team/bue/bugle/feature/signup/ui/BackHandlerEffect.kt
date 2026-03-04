package team.bue.bugle.feature.signup.ui

import androidx.compose.runtime.Composable

@Composable
expect fun SignUpBackHandlerEffect(
    enabled: Boolean,
    onBack: () -> Unit,
)
