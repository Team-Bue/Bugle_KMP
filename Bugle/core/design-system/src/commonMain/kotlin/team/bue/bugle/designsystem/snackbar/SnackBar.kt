package team.aliens.dms.android.core.designsystem.snackbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bugle.core.design_system.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.util.bugleGlowShadow

@Composable
fun BugleSnackBar(
    modifier: Modifier = Modifier,
    snackBarType: BugleSnackBarType,
    message: String,
) {
    Row(
        modifier = modifier
            .wrapContentWidth()
            .bugleGlowShadow(
                shape = CircleShape,
                color = BugleTheme.colors.onPrimaryContainer.copy(alpha = 0.1f),
                radius = 20.dp,
                spread = 4.dp,
            )
            .background(
                color = BugleTheme.colors.surfaceTint,
                shape = CircleShape,
            )
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Image(
            painter = painterResource(snackBarType.iconRes),
            contentDescription = null,
        )
        Text(
            text = message,
            style = BugleTypography.labelM,
            color = BugleTheme.colors.tertiaryContainer,
        )
    }
}

enum class BugleSnackBarType(
    val iconRes: DrawableResource,
) {
    // TODO 토스트 아이콘 구현
}
