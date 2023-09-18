package vip.mystery0.l.skipper.model

import vip.mystery0.l.skipper.store.SkipperStore

object RunningRule {
    var enable: Boolean = SkipperStore.enable
    var debug: Boolean = SkipperStore.debug
    val globalKeywords: MutableSet<String> = SkipperStore.globalKeywords.toMutableSet()
    val disabledAppList: MutableSet<String> = SkipperStore.disabledAppList.toMutableSet()
    val rules: Map<Int, Map<String, String>> = emptyMap()
    var interceptText: String = SkipperStore.interceptText

    override fun toString(): String {
        return "(enable=$enable, globalKeywords=$globalKeywords, disabledAppList=$disabledAppList, rules.size=${rules.size})"
    }
}