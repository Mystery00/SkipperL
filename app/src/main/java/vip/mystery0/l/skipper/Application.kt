package vip.mystery0.l.skipper

import android.app.Application
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import vip.mystery0.l.skipper.module.moduleList

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        //配置Koin
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(moduleList())
        }
        MMKV.initialize(this)
    }
}