package team.bue.bugle.designsystem.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import team.bue.bugle.designsystem.foundation.BugleTheme

@Composable
fun BugleIconButton(
    resource: DrawableResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    enabled: Boolean = true,
    size: Dp = 26.dp,
    contentPaddingValues: PaddingValues = PaddingValues(2.dp),
    contentDescription: String? = null,
) {
    val effectiveTint = if (tint == Color.Unspecified) BugleTheme.colors.onBackground else tint

    IconButton(
        modifier = modifier.size(size),
        enabled = enabled,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.padding(contentPaddingValues),
            painter = painterResource(resource),
            tint = effectiveTint,
            contentDescription = contentDescription,
        )
    }
}
