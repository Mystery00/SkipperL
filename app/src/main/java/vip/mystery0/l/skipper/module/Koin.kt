package vip.mystery0.l.skipper.module

import org.koin.core.module.Module

fun moduleList(): List<Module> =
    listOf(
        databaseModule,
        httpModule,
    )