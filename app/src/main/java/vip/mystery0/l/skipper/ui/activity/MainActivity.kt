package vip.mystery0.l.skipper.ui.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import kotlinx.coroutines.flow.MutableStateFlow
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import vip.mystery0.l.skipper.R
import vip.mystery0.l.skipper.appName
import vip.mystery0.l.skipper.services.MessageEvent
import vip.mystery0.l.skipper.services.SkipperService
import vip.mystery0.l.skipper.ui.tab.Tab
import vip.mystery0.l.skipper.ui.theme.Icons
import vip.mystery0.l.skipper.viewmodel.SkipperViewModel

val LocalAccessibilityServiceEnabled = compositionLocalOf<Boolean> {
    error("No LocalAccessibilityServiceEnabled provided")
}

class MainActivity : BaseComposeActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel: SkipperViewModel by viewModels()
    private var accessibilityServiceEnabled = MutableStateFlow(true)
    private val eventBus: EventBus by inject()
    private val accessibilityManager: AccessibilityManager by inject()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun BuildContent() {
        val isAccessibilityServiceEnabled by accessibilityServiceEnabled.collectAsState()
        var selectedIndex by remember { mutableIntStateOf(0) }
        val showDialog = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = appName) },
                    actions = {
                        if (!isAccessibilityServiceEnabled) {
                            IconButton(onClick = {
                                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                            }) {
                                Icon(
                                    painter = Icons.tips,
                                    contentDescription = null,
                                    tint = LocalContentColor.current,
                                )
                            }
                        }
                    },
                )
            },
            bottomBar = {
                AnimatedNavigationBar(
                    selectedIndex = selectedIndex,
                    barColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                    ballColor = MaterialTheme.colorScheme.secondary,
                ) {
                    DrawTab(
                        selectedIndex = selectedIndex,
                        tab = Tab.APPS,
                        onChangePage = { selectedIndex = it },
                    )
                    DrawTab(
                        selectedIndex = selectedIndex,
                        tab = Tab.STATISTICS,
                        onChangePage = { selectedIndex = it },
                    )
                    DrawTab(
                        selectedIndex = selectedIndex,
                        tab = Tab.SETTINGS,
                        onChangePage = { selectedIndex = it },
                    )
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                CompositionLocalProvider(LocalAccessibilityServiceEnabled provides isAccessibilityServiceEnabled) {
                    when (selectedIndex) {
                        Tab.APPS.index -> Tab.APPS.content(viewModel)
                        Tab.STATISTICS.index -> Tab.STATISTICS.content(viewModel)
                        Tab.SETTINGS.index -> Tab.SETTINGS.content(viewModel)
                    }
                }
            }
        }
        if (!isAccessibilityServiceEnabled) {
            showDialog.value = true
        }
        BuildDialog(show = showDialog)
    }

    @Composable
    private fun DrawTab(
        selectedIndex: Int,
        tab: Tab,
        onChangePage: (Int) -> Unit,
    ) {
        val checked = selectedIndex == tab.index
        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DropletButton(
                isSelected = checked,
                onClick = {
                    onChangePage(tab.index)
                },
                icon = tab.icon,
                iconColor = MaterialTheme.colorScheme.onSurface,
                dropletColor = MaterialTheme.colorScheme.primary,
                size = 24.dp,
            )
            Text(text = stringResource(id = tab.label))
        }
    }

    @Composable
    fun BuildDialog(show: MutableState<Boolean>) {
        if (!show.value) return
        AlertDialog(
            onDismissRequest = { show.value = false },
            title = {
                Text(stringResource(id = R.string.hint_accessibility_off))
            },
            text = {
                Text(stringResource(id = R.string.hint_enable_accessibility))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                        show.value = false
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        show.value = false
                    }
                ) {
                    Text(stringResource(android.R.string.cancel))
                }
            },
        )
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val accessibilityServices =
            accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN)
        return accessibilityServices.any { it.id == "${packageName}/${SkipperService::class.java.name}" }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @Suppress("unused")
    fun onMessageEvent(event: MessageEvent) {
        Log.i(TAG, "onAccessibilityServiceEnabledChange: $event")
        accessibilityServiceEnabled.value = event.enable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        eventBus.register(this)
        super.onCreate(savedInstanceState)
        accessibilityServiceEnabled.value = isAccessibilityServiceEnabled()
    }

    override fun onDestroy() {
        eventBus.unregister(this)
        super.onDestroy()
    }
}