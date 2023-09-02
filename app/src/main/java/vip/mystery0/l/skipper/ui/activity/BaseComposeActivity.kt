package vip.mystery0.l.skipper.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import vip.mystery0.l.skipper.ui.theme.SkipperLTheme

abstract class BaseComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkipperLTheme {
                BuildContent()
            }
        }
    }

    @Composable
    open fun BuildContent() {
    }
}