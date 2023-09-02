package vip.mystery0.l.skipper.ui.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vip.mystery0.l.skipper.ui.settings.SettingsGroup

val settingsTab:TabContent = @Composable {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        SettingsGroup(title = "设置") {

        }
    }
}