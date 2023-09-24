package vip.mystery0.l.skipper.module

import okhttp3.OkHttpClient
import org.koin.dsl.module

val httpModule = module {
    single {
        OkHttpClient()
    }
}