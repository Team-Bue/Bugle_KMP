package team.bue.bugle.designsystem.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.util.clickable

@Composable
fun BugleVerificationCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    totalSeconds: Int = 180,
    onResendClick: () -> Unit,
    onTimerFinished: () -> Unit = {},
) {
    var remainSeconds by remember(totalSeconds) { mutableIntStateOf(totalSeconds) }

    LaunchedEffect(remainSeconds) {
        if (remainSeconds == 0) {
            onTimerFinished()
        }
    }

    LaunchedEffect(totalSeconds, remainSeconds) {
        if (remainSeconds > 0) {
            delay(1_000)
            remainSeconds -= 1
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BugleVerificationCodeInput(
                code = code,
                onCodeChange = onCodeChange,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(16.dp))

        val timerColor = if (remainSeconds == 0) BugleTheme.colors.error else BugleColor.primary500
        BasicText(
            text = "${remainSeconds / 60}:${(remainSeconds % 60).toString().padStart(2, '0')}",
            style = BugleTypography.sLabelL.copy(
                color = timerColor,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicText(
                text = "코드가 도착하지 않았나요?",
                style = BugleTypography.textS.copy(color = BugleColor.gray700),
            )

            Spacer(Modifier.width(4.dp))

            BasicText(
                text = "재전송",
                style = BugleTypography.textS.copy(color = BugleColor.primary500),
                modifier = Modifier.clickable(
                    onClick = {
                        remainSeconds = totalSeconds
                        onResendClick()
                    },
                ),
            )
        }
    }
}

@Composable
private fun BugleVerificationCodeInput(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier,
    ) {
        BasicTextField(
            value = code,
            onValueChange = { value ->
                val filtered = value.filter(Char::isDigit).take(6)
                onCodeChange(filtered)
            },
            modifier = Modifier
                .focusRequester(focusRequester)
                .width(1.dp),
            decorationBox = {},
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(6) { index ->
                val char = code.getOrNull(index)?.toString().orEmpty()
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .aspectRatio(50f / 64f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BugleColor.gray900)
                        .clickable(onClick = { focusRequester.requestFocus() }),
                    contentAlignment = Alignment.Center,
                ) {
                    BasicText(
                        text = char,
                        style = BugleTypography.titleL.copy(
                            color = BugleColor.white,
                            textAlign = TextAlign.Center,
                        ),
                    )
                }
            }
        }
    }
}
