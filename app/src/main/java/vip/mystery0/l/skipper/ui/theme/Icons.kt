package vip.mystery0.l.skipper.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import vip.mystery0.l.skipper.R

object Icons {
    val appIcon: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_app_logo)
    val close: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_close)
    val tips: Painter
        @Composable
        get() = painterResource(id = R.drawable.ic_action_tips)

    object Settings {
        val arrow: Painter
            @Composable
            get() = painterResource(id = R.drawable.ic_settings_arrow)
        val enable: Painter
            @Composable
            get() = painterResource(id = R.drawable.ic_settings_enable)
        val keywords: Painter
            @Composable
            get() = painterResource(id = R.drawable.ic_settings_keywords)
        val status: Painter
            @Composable
            get() = painterResource(id = R.drawable.ic_settings_status)
    }
}