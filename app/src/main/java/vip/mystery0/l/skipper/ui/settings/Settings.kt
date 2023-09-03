package vip.mystery0.l.skipper.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SettingsClickableComp(
    icon: Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    SettingsScaffold(
        icon = {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(24.dp)
            )
        },
        title = title,
        subtitle = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        additionalContent = @Composable {
            Icon(
                Icons.Rounded.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null
            )
        },
        onClick = onClick,
    )
}

@Composable
fun SettingsClickableCustomSubtitleComp(
    icon: Painter,
    title: String,
    subtitle: @Composable () -> Unit,
    onClick: () -> Unit = {},
) {
    SettingsScaffold(
        icon = {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(24.dp)
            )
        },
        title = title,
        subtitle = subtitle,
        additionalContent = @Composable {
            Icon(
                Icons.Rounded.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null
            )
        },
        onClick = onClick,
    )
}

@Composable
fun SettingsClickableImageComp(
    icon: Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    SettingsScaffold(
        icon = {
            Image(
                painter = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(24.dp)
            )
        },
        title = title,
        subtitle = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        additionalContent = @Composable {
            Icon(
                Icons.Rounded.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null
            )
        },
        onClick = onClick,
    )
}

@Composable
fun SettingsSwitchComp(
    icon: Painter,
    title: String,
    subtitle: String,
    checked: Boolean,
    onClick: () -> Unit = {},
) {
    SettingsScaffold(
        icon = {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(24.dp)
            )
        },
        title = title,
        subtitle = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        additionalContent = @Composable {
            Switch(checked = checked, onCheckedChange = { onClick() })
        },
        onClick = onClick,
    )
}