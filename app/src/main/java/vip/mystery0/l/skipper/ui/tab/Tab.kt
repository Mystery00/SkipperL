package vip.mystery0.l.skipper.ui.tab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import vip.mystery0.l.skipper.R
import vip.mystery0.l.skipper.viewmodel.SkipperViewModel

internal enum class Tab(
    val index: Int,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    val content: TabContent,
) {
    APPS(
        0,
        R.string.tab_apps,
        icon = R.drawable.ic_tab_apps,
        content = appsTap,
    ),
    STATISTICS(
        1,
        R.string.tab_statistics,
        icon = R.drawable.ic_tab_statistics,
        content = recordsTap,
    ),
    SETTINGS(
        2,
        R.string.tab_settings,
        icon = R.drawable.ic_tab_settings,
        content = settingsTab,
    ),
}

typealias TabContent = @Composable (SkipperViewModel) -> Unit