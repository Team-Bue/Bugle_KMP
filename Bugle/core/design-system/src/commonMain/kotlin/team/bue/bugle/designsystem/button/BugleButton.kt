package team.bue.bugle.designsystem.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.util.bugleGlowShadow
import team.bue.bugle.designsystem.util.clickable

private val ButtonShape = RoundedCornerShape(32.dp)
private val GlowColor = Color(0x99FF4FB6)

@Composable
fun BugleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor = when {
        !enabled -> BugleColor.primary200
        isPressed -> BugleColor.primary700
        isHovered -> BugleColor.primary600
        else -> BugleColor.primary500
    }
    Box(
        modifier = modifier
            .bugleGlowShadow(
                shape = ButtonShape,
                color = GlowColor,
                radius = 16.dp,
                spread = 4.dp,
            )
            .clip(ButtonShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                pressDepth = 1f,
                onClick = onClick,
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = BugleTypography.sLabelL.copy(
                color = BugleTheme.colors.onPrimary,
                textAlign = TextAlign.Center,
            ),
        )
    }
}
