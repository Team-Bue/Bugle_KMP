package team.bue.bugle.designsystem.button

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import team.bue.bugle.designsystem.util.clickable

@Composable
fun BugleToggleIconButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onToggle: () -> Unit,
    selectedIcon: DrawableResource,
    unselectedIcon: DrawableResource,
    selectedTint: Color = Color.Unspecified,
    unselectedTint: Color = Color.Unspecified,
    size: Dp = 20.dp,
    contentDescription: String? = null,
) {
    val unselectedScale by animateFloatAsState(
        targetValue = if (isSelected) 0f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    )
    val selectedScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
    )

    Box(
        modifier = modifier
            .size(size)
            .clickable(
                indication = null,
                onClick = onToggle,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .scale(unselectedScale)
                .alpha(unselectedScale),
            painter = painterResource(unselectedIcon),
            contentDescription = contentDescription,
            tint = unselectedTint,
        )
        Icon(
            modifier = Modifier
                .fillMaxSize()
                .scale(selectedScale)
                .alpha(selectedScale),
            painter = painterResource(selectedIcon),
            contentDescription = contentDescription,
            tint = selectedTint,
        )
    }
}
