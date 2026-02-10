package team.bue.bugle.designsystem.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val bugleLightColorScheme = lightColorScheme(
    primary = BugleColor.Light.primary500,
    onPrimary = BugleColor.Light.white,
    primaryContainer = BugleColor.Light.primary100,
    onPrimaryContainer = BugleColor.Light.primary900,
    inversePrimary = BugleColor.Light.primary200,
    secondary = BugleColor.Light.primary300,
    onSecondary = BugleColor.Light.white,
    secondaryContainer = BugleColor.Light.primary50,
    onSecondaryContainer = BugleColor.Light.primary800,
    surface = BugleColor.Light.gray50,
    onSurface = BugleColor.Light.gray900,
    surfaceVariant = BugleColor.Light.gray200,
    onSurfaceVariant = BugleColor.Light.gray700,
    inverseSurface = BugleColor.Light.gray800,
    inverseOnSurface = BugleColor.Light.gray100,
    surfaceTint = BugleColor.Light.primary500,
    surfaceBright = BugleColor.Light.gray50,
    surfaceContainer = BugleColor.Light.container,
    error = BugleColor.Light.error500,
    onError = BugleColor.Light.white,
    errorContainer = BugleColor.Light.error100,
    onErrorContainer = BugleColor.Light.error900,
    outline = BugleColor.Light.gray400,
    outlineVariant = BugleColor.Light.gray300,
    background = BugleColor.Light.background,
    onBackground = BugleColor.Light.black,
    tertiary = BugleColor.Light.success500,
    onTertiary = BugleColor.Light.white,
    tertiaryContainer = BugleColor.Light.success100,
    onTertiaryContainer = BugleColor.Light.success900,
    scrim = BugleColor.Light.black,
)

private val bugleDarkColorScheme = darkColorScheme(
    primary = BugleColor.Dark.primary500,
    onPrimary = BugleColor.Dark.white,
    primaryContainer = BugleColor.Dark.primary100,
    onPrimaryContainer = BugleColor.Dark.primary900,
    inversePrimary = BugleColor.Dark.primary200,
    secondary = BugleColor.Dark.primary300,
    onSecondary = BugleColor.Dark.white,
    secondaryContainer = BugleColor.Dark.primary50,
    onSecondaryContainer = BugleColor.Dark.primary800,
    surface = BugleColor.Dark.gray50,
    onSurface = BugleColor.Dark.gray900,
    surfaceVariant = BugleColor.Dark.gray200,
    onSurfaceVariant = BugleColor.Dark.gray700,
    inverseSurface = BugleColor.Dark.gray800,
    inverseOnSurface = BugleColor.Dark.gray100,
    surfaceTint = BugleColor.Dark.primary500,
    surfaceBright = BugleColor.Dark.gray50,
    surfaceContainer = BugleColor.Dark.container,
    error = BugleColor.Dark.error500,
    onError = BugleColor.Dark.white,
    errorContainer = BugleColor.Dark.error100,
    onErrorContainer = BugleColor.Dark.error900,
    outline = BugleColor.Dark.gray400,
    outlineVariant = BugleColor.Dark.gray300,
    background = BugleColor.Dark.background,
    onBackground = BugleColor.Dark.black,
    tertiary = BugleColor.Dark.success500,
    onTertiary = BugleColor.Dark.white,
    tertiaryContainer = BugleColor.Dark.success100,
    onTertiaryContainer = BugleColor.Dark.success900,
    scrim = BugleColor.Dark.black,
)

val LocalBugleColors = staticCompositionLocalOf<ColorScheme> { lightColorScheme() }

@Composable
fun BugleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        bugleDarkColorScheme
    } else {
        bugleLightColorScheme
    }

    CompositionLocalProvider(LocalBugleColors provides colors) {
        content()
    }
}

object BugleTheme {
    val colors
        @Composable get() = LocalBugleColors.current
}
