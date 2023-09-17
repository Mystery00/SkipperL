package vip.mystery0.l.skipper.ui.tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.pullrefresh.PullRefreshIndicator
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import vip.mystery0.l.skipper.ui.settings.SettingsScaffold
import vip.mystery0.l.skipper.viewmodel.ConfigApp

val appsTap: TabContent = @Composable { viewModel ->
    val appState by viewModel.appState.collectAsState()
    val disabledAppList = viewModel.disabledAppList

    val pullRefreshState = rememberPullRefreshState(
        refreshing = appState.refreshing,
        onRefresh = { viewModel.refreshAppList() },
    )

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(appState.list) { app ->
                BuildAppItem(
                    app = app,
                    enable = !disabledAppList.contains(app.packageName),
                ) { enable ->
                    if (enable) {
                        viewModel.updateDisabledAppList(disabledAppList.toSet() - app.packageName)
                    } else {
                        viewModel.updateDisabledAppList(disabledAppList.toSet() + app.packageName)
                    }
                }
            }
        }
        PullRefreshIndicator(
            refreshing = appState.refreshing,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
fun BuildAppItem(
    app: ConfigApp,
    enable: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    SettingsScaffold(
        icon = {
            AsyncImage(
                model = app.icon,
                contentDescription = app.appName,
                modifier = Modifier
                    .size(48.dp)
            )
        },
        title = app.appName,
        subtitle = {
            Text(
                text = app.packageName,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        additionalContent = @Composable {
            Switch(checked = enable, onCheckedChange = onCheckedChange)
        },
        onClick = {},
    )
}