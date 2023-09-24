package vip.mystery0.l.skipper.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import vip.mystery0.l.skipper.dao.RuleDao
import vip.mystery0.l.skipper.store.SkipperStore

object RunningRule {
    var enable: Boolean = SkipperStore.enable
    var debug: Boolean = SkipperStore.debug
    val globalKeywords: MutableSet<String> = SkipperStore.globalKeywords.toMutableSet()
    val disabledAppList: MutableSet<String> = SkipperStore.disabledAppList.toMutableSet()
    val rules: MutableMap<Int, Map<String, String>> = hashMapOf()
    var interceptText: String = SkipperStore.interceptText

    suspend fun loadRules() {
        val ruleDao = KoinJavaComponent.get<RuleDao>(RuleDao::class.java)
        val rules = ruleDao.getAll()
        val ruleMap = HashMap<Int, HashMap<String, String>>(rules.size)
        withContext(Dispatchers.Default) {
            rules.forEach {
                val hashCode = it.nodeId.toInt()
                val actionMap = ruleMap[hashCode] ?: HashMap()
                actionMap[it.actionId] = it.actionText
                ruleMap[hashCode] = actionMap
            }
        }
        this.rules.clear()
        this.rules.putAll(ruleMap)
    }

    override fun toString(): String {
        return "(enable=$enable, globalKeywords=$globalKeywords, disabledAppList=$disabledAppList, rules.size=${rules.values.sumOf { it.size }})"
    }
}