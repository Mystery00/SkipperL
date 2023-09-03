package vip.mystery0.l.skipper.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import vip.mystery0.l.skipper.ui.theme.Icons

@Composable
fun SettingsClickableComp(
    icon: Painter,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
) {
    SettingsClickableCustomSubtitleComp(
        icon = icon,
        title = title,
        subtitle = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
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
                Icons.Settings.arrow,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null,
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
                Icons.Settings.arrow,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null,
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

@Composable
fun SettingsTextComp(
    icon: Painter,
    title: String,
    subtitle: String,
    currentValue: String,
    onSave: (String) -> Unit,
    onCheck: (String) -> Boolean,
) {
    SettingsTextCustomSubtitleComp(
        icon = icon,
        title = title,
        subtitle = {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        currentValue = currentValue,
        onSave = onSave,
        onCheck = onCheck,
    )
}

@Composable
fun SettingsTextCustomSubtitleComp(
    icon: Painter,
    title: String,
    subtitle: @Composable () -> Unit,
    currentValue: String,
    onSave: (String) -> Unit,
    onCheck: (String) -> Boolean,
) {
    var isDialogShown by remember { mutableStateOf(false) }

    if (isDialogShown) {
        TextEditDialog(
            onDismissRequest = {
                isDialogShown = false
            },
            title = title,
            storedValue = currentValue,
            onSave = onSave,
            onCheck = onCheck,
        )
    }

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
                Icons.Settings.arrow,
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null,
            )
        },
        onClick = { isDialogShown = true },
    )
}