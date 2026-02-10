package team.bue.bugle.designsystem.foundation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bugle.core.design_system.generated.resources.Res
import bugle.core.design_system.generated.resources.pretendard_bold
import bugle.core.design_system.generated.resources.pretendard_medium
import bugle.core.design_system.generated.resources.pretendard_regular
import org.jetbrains.compose.resources.Font

@Composable
private fun pretendardFamily() = FontFamily(
    Font(
        resource = Res.font.pretendard_regular,
        weight = FontWeight.Light,
    ),
    Font(
        resource = Res.font.pretendard_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resource = Res.font.pretendard_bold,
        weight = FontWeight.Bold,
    ),
)

object BugleTypography {
    val headlineS
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        )

    val headlineM
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        )

    val titleL
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )

    val titleM
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )

    val titleS
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        )

    val sTitleL
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        )

    val sTitleM
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        )

    val sTitleS
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        )

    val labelL
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        )

    val labelM
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        )

    val labelS
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        )

    val sLabelL
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
            letterSpacing = 0.sp,
        )

    val sLabelM
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp,
            letterSpacing = 0.sp,
        )

    val sLabelS
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 16.sp,
            letterSpacing = 0.sp,
        )

    val textL
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 12.sp,
            letterSpacing = 0.sp,
        )

    val textM
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 12.sp,
            letterSpacing = 0.sp,
        )

    val textS
        @Composable get() = TextStyle(
            fontFamily = pretendardFamily(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 12.sp,
            letterSpacing = 0.sp,
        )
}
