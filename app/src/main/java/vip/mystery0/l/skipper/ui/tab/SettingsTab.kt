package vip.mystery0.l.skipper.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import vip.mystery0.l.skipper.appVersionCode
import vip.mystery0.l.skipper.appVersionName
import vip.mystery0.l.skipper.ui.settings.SettingsClickableCustomSubtitleComp
import vip.mystery0.l.skipper.ui.settings.SettingsClickableImageComp
import vip.mystery0.l.skipper.ui.settings.SettingsGroup
import vip.mystery0.l.skipper.ui.settings.SettingsSwitchComp
import vip.mystery0.l.skipper.ui.theme.Icons

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
val settingsTab: TabContent = @Composable { viewModel ->
    val scope = rememberCoroutineScope()
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
            SettingsClickableCustomSubtitleComp(
                icon = Icons.Settings.keywords,
                title = "全局关键词",
                subtitle = {
                    FlowRow {
                        globalKeywords.forEach {
                            FilterChip(selected = true, onClick = { }, label = { Text(text = it) })
                        }
                    }
                })
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