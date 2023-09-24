package vip.mystery0.l.skipper.ui.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import vip.mystery0.l.skipper.ui.theme.Icons
import vip.mystery0.l.skipper.viewmodel.RecordApp

val recordsTap: TabContent = @Composable { viewModel ->
    val recordState by viewModel.recordState.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = recordState.refreshing,
        onRefresh = { viewModel.refreshRecordAppList() },
    )

    Box(
        modifier = Modifier.pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(recordState.list) { app ->
                BuildRecordAppItem(app)
            }
        }
        PullRefreshIndicator(
            refreshing = recordState.refreshing,
            state = pullRefreshState,
            Modifier.align(Alignment.TopCenter),
        )
    }
}

@Composable
fun BuildRecordAppItem(app: RecordApp) {
    SettingsScaffold(
        icon = {
            if (app.icon != null) {
                AsyncImage(
                    model = app.icon,
                    contentDescription = app.appName,
                    modifier = Modifier
                        .size(48.dp)
                )
            } else {
                Image(
                    painter = Icons.appIcon,
                    contentDescription = app.appName,
                    modifier = Modifier
                        .size(48.dp)
                )
            }
        },
        title = app.appName,
        subtitle = {
            Text(
                text = app.packageName,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        additionalContent = @Composable {
            Text(
                text = app.triggerCount.toString(),
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        onClick = {},
    )
}