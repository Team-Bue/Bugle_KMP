package team.bue.bugle.designsystem.foundation

import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import team.bue.bugle.designsystem.foundation.BugleTheme.colors

private val bugleColorScheme = lightColorScheme(
    primary = BugleColor.primary500,
    onPrimary = BugleColor.white,
    primaryContainer = BugleColor.primary100,
    onPrimaryContainer = BugleColor.primary900,
    inversePrimary = BugleColor.primary200,
    secondary = BugleColor.primary400,
    onSecondary = BugleColor.primary800,
    secondaryContainer = BugleColor.primary300,
    onSecondaryContainer = BugleColor.primary700,
    tertiary = BugleColor.warning500,
    onTertiary = BugleColor.warning700,
    tertiaryContainer = BugleColor.warning100,
    onTertiaryContainer = BugleColor.primary600,
    background = BugleColor.background,
    onBackground = BugleColor.black,
    surface = BugleColor.gray50,
    onSurface = BugleColor.gray800,
    surfaceVariant = BugleColor.gray200,
    onSurfaceVariant = BugleColor.gray600,
    surfaceTint = BugleColor.primary50,
    inverseSurface = BugleColor.gray700,
    inverseOnSurface = BugleColor.gray100,
    error = BugleColor.error500,
    onError = BugleColor.error50,
    errorContainer = BugleColor.error100,
    onErrorContainer = BugleColor.error900,
    outline = BugleColor.pressed,
    outlineVariant = BugleColor.error400,
    scrim = BugleColor.gray900,
    surfaceBright = BugleColor.error200,
    surfaceDim = BugleColor.error800,
    surfaceContainer = BugleColor.gray400,
    surfaceContainerHigh = BugleColor.hover,
    surfaceContainerHighest = BugleColor.gray500,
    surfaceContainerLow = BugleColor.button,
    surfaceContainerLowest = BugleColor.gray300,
)

val LocalBugleColors = staticCompositionLocalOf { bugleColorScheme }

@Composable
fun BugleTheme(
    content: @Composable () -> Unit,
) {


    CompositionLocalProvider(LocalBugleColors provides colors) {
        content()
    }
}

object BugleTheme {
    val colors
        @Composable get() = LocalBugleColors.current
}
