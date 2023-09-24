package vip.mystery0.l.skipper.module

import android.view.accessibility.AccessibilityManager
import androidx.activity.ComponentActivity
import org.greenrobot.eventbus.EventBus
import org.koin.core.module.Module
import org.koin.dsl.module
import vip.mystery0.l.skipper.context

fun moduleList(): List<Module> =
    listOf(
        appModule,
        databaseModule,
        httpModule,
    )

val appModule = module {
    single { context.getSystemService(ComponentActivity.ACCESSIBILITY_SERVICE) as AccessibilityManager }
    single { EventBus.getDefault() }
}