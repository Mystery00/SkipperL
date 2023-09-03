package vip.mystery0.l.skipper.ui.tab

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import vip.mystery0.l.skipper.R
import vip.mystery0.l.skipper.viewmodel.SkipperViewModel

internal enum class Tab(
    val index: Int,
    val label: String,
    @DrawableRes val icon: Int,
    val content: TabContent,
) {
    APPS(
        0,
        "应用",
        icon = R.drawable.ic_tab_apps,
        content = appsTap,
    ),
    STATISTICS(
        1,
        "统计",
        icon = R.drawable.ic_tab_statistics,
        content = {

        }
    ),
    SETTINGS(
        2,
        "设置",
        icon = R.drawable.ic_tab_settings,
        content = settingsTab,
    ),
}

typealias TabContent = @Composable (SkipperViewModel) -> Unit