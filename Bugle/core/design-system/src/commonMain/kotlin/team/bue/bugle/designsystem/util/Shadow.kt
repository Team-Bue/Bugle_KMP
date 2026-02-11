package team.bue.bugle.designsystem.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.bugleGlowShadow(
    shape: Shape,
    color: Color,
    radius: Dp,
    spread: Dp,
): Modifier = this.dropShadow(
    shape = shape,
    shadow = Shadow(radius = radius, spread = spread, color = color),
)
