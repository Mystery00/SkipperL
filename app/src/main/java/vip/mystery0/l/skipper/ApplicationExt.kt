package vip.mystery0.l.skipper

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal lateinit var context: Context

//应用名称
val appName: String
    get() = context.getString(R.string.app_name)

//版本名称
val appVersionName: String
    get() = context.getString(R.string.app_version_name)

//版本号
val appVersionCode: String
    get() = context.getString(R.string.app_version_code)