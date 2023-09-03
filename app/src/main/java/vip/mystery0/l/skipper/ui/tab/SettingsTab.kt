package vip.mystery0.l.skipper.ui.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import vip.mystery0.l.skipper.appVersionCode
import vip.mystery0.l.skipper.appVersionName
import vip.mystery0.l.skipper.ui.settings.SettingsClickableImageComp
import vip.mystery0.l.skipper.ui.settings.SettingsGroup
import vip.mystery0.l.skipper.ui.settings.SettingsSwitchComp
import vip.mystery0.l.skipper.ui.settings.SettingsTextCustomSubtitleComp
import vip.mystery0.l.skipper.ui.theme.Icons

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
val settingsTab: TabContent = @Composable { viewModel ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        SettingsGroup(title = "设置") {
            val enable by viewModel.globalEnable.collectAsState()
            SettingsSwitchComp(
                icon = Icons.Settings.enable,
                title = "全局启用",
                subtitle = "全局启用",
                checked = enable,
            ) {
                viewModel.changeGlobalEnable(!enable)
            }
            val globalKeywords = viewModel.globalKeywords
            SettingsTextCustomSubtitleComp(
                icon = Icons.Settings.keywords,
                title = "全局关键词",
                subtitle = {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        globalKeywords.forEach { keyword ->
                            InputChip(
                                selected = true,
                                onClick = {
                                    viewModel.updateGlobalKeywords(globalKeywords.filter { it != keyword }
                                        .toSet())
                                },
                                label = { Text(text = keyword) },
                                trailingIcon = {
                                    Icon(painter = Icons.close, contentDescription = null)
                                })
                        }
                    }
                },
                currentValue = "",
                onSave = { input ->
                    val inputStr = input.trim()
                    if (inputStr.isNotEmpty()) {
                        viewModel.updateGlobalKeywords(globalKeywords.toSet() + inputStr)
                    }
                },
                onCheck = { input -> input.trim().isNotEmpty() },
            )
        }
        SettingsGroup(title = "关于") {
            SettingsClickableImageComp(
                icon = Icons.appIcon,
                title = "应用版本",
                subtitle = "${appVersionName}(${appVersionCode})",
                onClick = {},
            )
        }
    }
}