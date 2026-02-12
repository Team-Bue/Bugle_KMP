package team.bue.bugle.designsystem.foundation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class BugleTokens(
    val button: Color = BugleColor.button,
    val hover: Color = BugleColor.hover,
    val pressed: Color = BugleColor.pressed,
)

val LocalBugleTokens = staticCompositionLocalOf { BugleTokens() }
