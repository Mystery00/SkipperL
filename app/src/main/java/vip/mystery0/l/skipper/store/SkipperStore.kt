package vip.mystery0.l.skipper.store

import com.tencent.mmkv.MMKV

object SkipperStore {
    private val kv = MMKV.defaultMMKV()

    //全局启用开关
    private const val enableKey = "enable"
    var enable: Boolean
        set(value) {
            kv.encode(enableKey, value)
        }
        get() {
            return kv.decodeBool(enableKey, true)
        }

    //调试模式
    private const val debugKey = "debug"
    var debug: Boolean
        set(value) {
            kv.encode(debugKey, value)
        }
        get() {
            return kv.decodeBool(debugKey, true)
        }

    //拦截文本
    private const val interceptTextKey = "interceptText"
    var interceptText: String
        set(value) {
            kv.encode(interceptTextKey, value)
        }
        get() {
            return kv.decodeString(interceptTextKey) ?: "侦测到在途的导弹攻击，已成功拦截"
        }

    //全局关键词
    private const val globalKeywordsKey = "globalKeywords"
    var globalKeywords: Set<String>
        set(value) {
            kv.encode(globalKeywordsKey, value)
        }
        get() {
            return kv.decodeStringSet(globalKeywordsKey) ?: setOf("跳过")
        }

    //禁用的应用列表
    private const val disabledAppListKey = "disabledAppList"
    var disabledAppList: Set<String>
        set(value) {
            kv.encode(disabledAppListKey, value)
        }
        get() {
            return kv.decodeStringSet(disabledAppListKey) ?: emptySet()
        }
}