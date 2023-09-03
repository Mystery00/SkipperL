package vip.mystery0.l.skipper.ui.activity

import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.items.dropletbutton.DropletButton
import vip.mystery0.l.skipper.appName
import vip.mystery0.l.skipper.ui.tab.Tab
import vip.mystery0.l.skipper.viewmodel.SkipperViewModel

class MainActivity : BaseComposeActivity() {
    private val viewModel: SkipperViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun BuildContent() {
        var selectedIndex by remember { mutableStateOf(0) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = appName) })
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
                when (selectedIndex) {
                    Tab.APPS.index -> Tab.APPS.content(viewModel)
                    Tab.STATISTICS.index -> Tab.STATISTICS.content(viewModel)
                    Tab.SETTINGS.index -> Tab.SETTINGS.content(viewModel)
                }
            }
        }
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
            Text(text = tab.label)
        }
    }
}