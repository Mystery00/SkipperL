package vip.mystery0.l.skipper

import android.app.Application
import com.tencent.mmkv.MMKV

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        context = this
        MMKV.initialize(this)
    }
}