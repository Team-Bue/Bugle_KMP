package team.bue.bugle.designsystem.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.util.clickable

private val SocialButtonShape = RoundedCornerShape(100.dp)

@Composable
fun BugleSocialButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
) {
    val backgroundColor = if (enabled) BugleColor.Light.black else BugleColor.Light.gray900
    val textColor = if (enabled) Color.White else BugleColor.Light.gray500
    val borderColor = BugleColor.Light.gray700

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(SocialButtonShape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = SocialButtonShape,
            )
            .background(backgroundColor)
            .clickable(
                enabled = enabled,
                indication = null,
                onClick = onClick,
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp)
                .size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }

        BasicText(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            style = BugleTypography.sLabelL.copy(
                color = textColor,
                textAlign = TextAlign.Center,
            ),
        )
    }
}
