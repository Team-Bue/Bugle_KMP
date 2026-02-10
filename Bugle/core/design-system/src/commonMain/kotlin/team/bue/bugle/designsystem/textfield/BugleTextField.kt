package team.bue.bugle.designsystem.textfield

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import bugle.core.design_system.generated.resources.Res
import bugle.core.design_system.generated.resources.ic_cancel
import bugle.core.design_system.generated.resources.ic_error
import bugle.core.design_system.generated.resources.ic_eye
import bugle.core.design_system.generated.resources.ic_eye_off
import org.jetbrains.compose.resources.painterResource
import team.bue.bugle.designsystem.button.BugleIconButton
import team.bue.bugle.designsystem.foundation.BugleColor
import team.bue.bugle.designsystem.foundation.BugleTypography

@Composable
fun BugleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    showVisibleIcon: Boolean = false,
    showClearIcon: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val borderColor = if (isError) BugleColor.Light.error500 else BugleColor.Light.primary500
    var visible by remember { mutableStateOf(false) }
    val (visualTransformation, icon) = if (visible || !showVisibleIcon) {
        VisualTransformation.None to Res.drawable.ic_eye
    } else {
        PasswordVisualTransformation() to Res.drawable.ic_eye_off
    }

    Column(modifier = modifier) {
        if (label.isNotEmpty()) {
            BasicText(
                text = label,
                style = BugleTypography.textL.copy(color = Color.White),
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = BugleTypography.sLabelL.copy(color = Color.White),
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(BugleColor.Light.primary500),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .drawBottomBorder(borderColor)
                        .padding(start = 8.dp, end = 12.dp, top = 18.dp, bottom = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder.isNotEmpty()) {
                            BasicText(
                                text = placeholder,
                                style = BugleTypography.sLabelL.copy(
                                    color = BugleColor.Light.gray700,
                                ),
                            )
                        }
                        innerTextField()
                    }
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        if (showVisibleIcon) {
                            BugleIconButton(
                                resource = icon,
                                onClick = { visible = !visible },
                                size = 24.dp,
                                tint = BugleColor.Light.gray500,
                            )
                        }
                        if (showClearIcon && value.isNotEmpty()) {
                            BugleIconButton(
                                resource = Res.drawable.ic_cancel,
                                onClick = { onValueChange("") },
                                size = 24.dp,
                                tint = BugleColor.Light.gray500,
                            )
                        }
                        if (trailingIcon != null) {
                            trailingIcon()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        if (isError && errorMessage.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_error),
                    contentDescription = null,
                    tint = BugleColor.Light.error500,
                )
                BasicText(
                    text = errorMessage,
                    style = BugleTypography.textM.copy(
                        color = BugleColor.Light.error500,
                    ),
                )
            }
        }
    }
}

private fun Modifier.drawBottomBorder(color: Color): Modifier =
    this.drawBehind {
        drawLine(
            color = color,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2.dp.toPx(),
        )
    }
