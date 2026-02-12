package team.bue.bugle.designsystem.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleTheme
import team.bue.bugle.designsystem.foundation.BugleTypography
import team.bue.bugle.designsystem.util.clickable

private val SocialButtonShape = RoundedCornerShape(100.dp)

@Composable
fun BugleSocialButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
) {
    val backgroundColor = if (enabled) BugleTheme.colors.onBackground else BugleColor.gray900
    val textColor = if (enabled) BugleTheme.colors.inverseOnSurface else BugleTheme.colors.onSurfaceVariant
    val borderColor = BugleTheme.tokens.pressed

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .semantics(mergeDescendants = true) {
                role = Role.Button
            }
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(4.dp))
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            icon()
        }
        BasicText(
            text = text,
            modifier = Modifier.weight(1f),
            style = BugleTypography.sLabelL.copy(
                color = textColor,
                textAlign = TextAlign.Center,
            ),
        )
        Spacer(Modifier.width(52.dp))
    }
}
