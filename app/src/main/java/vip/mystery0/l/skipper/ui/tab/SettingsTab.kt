package vip.mystery0.l.skipper.ui.tab

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import vip.mystery0.l.skipper.appVersionCode
import vip.mystery0.l.skipper.appVersionName
import vip.mystery0.l.skipper.model.RunningRule
import vip.mystery0.l.skipper.ui.activity.LocalAccessibilityServiceEnabled
import vip.mystery0.l.skipper.ui.settings.SettingsClickableComp
import vip.mystery0.l.skipper.ui.settings.SettingsClickableImageComp
import vip.mystery0.l.skipper.ui.settings.SettingsGroup
import vip.mystery0.l.skipper.ui.settings.SettingsSwitchComp
import vip.mystery0.l.skipper.ui.settings.SettingsTextCustomSubtitleComp
import vip.mystery0.l.skipper.ui.theme.Icons
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
val settingsTab: TabContent = @Composable { viewModel ->
    val lastUpdateTime by viewModel.lastUpdateTime.collectAsState()
    val ruleState by viewModel.ruleState.collectAsState()

    val showRunningDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Card(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    val isAccessibilityServiceEnabled = LocalAccessibilityServiceEnabled.current
                    if (isAccessibilityServiceEnabled) {
                        Text(
                            "规则更新时间：${
                                Instant.ofEpochMilli(lastUpdateTime).atZone(ZoneId.systemDefault())
                                    .format(
                                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                                    )
                            }"
                        )
                        Text(
                            "规则数量：${RunningRule.rules.values.sumOf { it.size }}"
                        )
                        Button(
                            enabled = ruleState.refreshing.not(),
                            onClick = { viewModel.refreshRuleList() },
                        ) {
                            Text("更新规则")
                        }
                    } else {
                        val context = LocalContext.current
                        Text("请先启用无障碍服务")
                        Button(
                            onClick = {
                                context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                            },
                        ) {
                            Text("跳转到无障碍设置")
                        }
                    }
                }
            }
        }
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
                                    viewModel.updateGlobalKeywords(globalKeywords.toSet() - keyword)
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
            SettingsClickableComp(
                icon = Icons.Settings.status,
                title = "查看当前运行规则状态",
                subtitle = "打印当前运行规则状态，用于调试",
                onClick = {
                    showRunningDialog.value = true
                }
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

    BuildRunningDialog(show = showRunningDialog)
    if (ruleState.message.isNotBlank()) {
        AlertDialog(
            onDismissRequest = { viewModel.clearRuleMessage() },
            title = {
                Text("更新规则失败")
            },
            text = {
                Text(ruleState.message)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearRuleMessage()
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
        )
    }
}

@Composable
fun BuildRunningDialog(show: MutableState<Boolean>) {
    if (!show.value) return
    AlertDialog(
        onDismissRequest = { show.value = false },
        title = {
            Text("当前运行规则")
        },
        text = {
            Text(RunningRule.toString())
        },
        confirmButton = {
            TextButton(
                onClick = {
                    show.value = false
                }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
    )
}