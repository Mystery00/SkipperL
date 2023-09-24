package vip.mystery0.l.skipper.viewmodel

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import vip.mystery0.l.skipper.context
import vip.mystery0.l.skipper.dao.RecordDao
import vip.mystery0.l.skipper.dao.RuleDao
import vip.mystery0.l.skipper.model.RunningRule
import vip.mystery0.l.skipper.model.db.CustomRule
import vip.mystery0.l.skipper.store.SkipperStore
import java.time.Instant
import java.time.temporal.ChronoUnit

class SkipperViewModel : ViewModel(), KoinComponent {
    companion object {
        private const val TAG = "SkipperViewModel"
    }

    private val httpClient: OkHttpClient by inject()
    private val recordDao: RecordDao by inject()
    private val ruleDao: RuleDao by inject()

    //全局禁用开关
    private val _globalEnable = MutableStateFlow(RunningRule.enable)
    val globalEnable: StateFlow<Boolean> = _globalEnable

    //全局关键词
    val globalKeywords = mutableStateListOf<String>()

    //所有应用列表
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState

    //触发应用列表
    private val _recordState = MutableStateFlow(RecordState())
    val recordState: StateFlow<RecordState> = _recordState

    //规则列表
    private val _ruleState = MutableStateFlow(RuleState())
    val ruleState: StateFlow<RuleState> = _ruleState

    //禁用应用列表
    val disabledAppList = mutableStateListOf<String>()

    //规则上一次更新时间
    private val _lastUpdateTime = MutableStateFlow(SkipperStore.lastUpdateTime)
    val lastUpdateTime: StateFlow<Long> = _lastUpdateTime

    init {
        viewModelScope.launch {
            globalKeywords.addAll(SkipperStore.globalKeywords)
            disabledAppList.addAll(SkipperStore.disabledAppList)
        }
        refreshAppList()
        refreshRecordAppList()
    }

    fun changeGlobalEnable(enable: Boolean) {
        viewModelScope.launch {
            SkipperStore.enable = enable
            RunningRule.enable = enable
            _globalEnable.value = enable
        }
    }

    fun updateGlobalKeywords(keywords: Set<String>) {
        viewModelScope.launch {
            SkipperStore.globalKeywords = keywords
            RunningRule.globalKeywords.clear()
            RunningRule.globalKeywords.addAll(keywords)
            globalKeywords.clear()
            globalKeywords.addAll(keywords)
        }
    }

    fun updateDisabledAppList(list: Set<String>) {
        viewModelScope.launch {
            SkipperStore.disabledAppList = list
            RunningRule.disabledAppList.clear()
            RunningRule.disabledAppList.addAll(list)
            disabledAppList.clear()
            disabledAppList.addAll(list)
        }
    }

    fun refreshAppList() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "refreshAppList: ", throwable)
            _appState.value = AppState(message = throwable.message ?: throwable.javaClass.name)
        }) {
            _appState.value = AppState(refreshing = true)
            _appState.value = AppState(list = getAllLauncherIconPackages())
        }
    }

    fun refreshRecordAppList() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "refreshRecordAppList: ", throwable)
            _recordState.value =
                RecordState(message = throwable.message ?: throwable.javaClass.name)
        }) {
            _recordState.value = RecordState(refreshing = true)
            val records = withContext(Dispatchers.IO) {
                recordDao.getAll(Instant.now().minus(7, ChronoUnit.DAYS).toEpochMilli())
            }
            if (records.isEmpty()) {
                _recordState.value = RecordState(list = emptyList())
                return@launch
            }
            val appMap = withContext(Dispatchers.Default) {
                getAllLauncherIconPackages().associateBy { it.packageName }
            }
            val recordAppList = withContext(Dispatchers.Default) {
                records.map {
                    RecordApp(
                        packageName = it.packageName,
                        appName = appMap[it.packageName]?.appName ?: it.packageName,
                        icon = appMap[it.packageName]?.icon,
                        triggerCount = it.count,
                    )
                }
            }
            _recordState.value = RecordState(list = recordAppList)
        }
    }

    fun refreshRuleList() {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG, "refreshRuleList: ", throwable)
            _ruleState.value = RuleState(message = throwable.message ?: throwable.javaClass.name)
        }) {
            _ruleState.value = RuleState(refreshing = true)
            val rules = fetchAllCustomRule()
            withContext(Dispatchers.IO) {
                ruleDao.deleteAll()
                ruleDao.insertAll(*rules.toTypedArray())
            }
            val ruleMap = HashMap<Int, HashMap<String, String>>(rules.size)
            withContext(Dispatchers.Default) {
                rules.forEach {
                    val hashCode = it.nodeId.toInt()
                    val actionMap = ruleMap[hashCode] ?: HashMap()
                    actionMap[it.actionId] = it.actionText
                    ruleMap[hashCode] = actionMap
                }
            }
            RunningRule.rules.clear()
            RunningRule.rules.putAll(ruleMap)
            SkipperStore.lastUpdateTime = System.currentTimeMillis()
            _lastUpdateTime.value = SkipperStore.lastUpdateTime
            _ruleState.value = RuleState()
        }
    }

    private fun getAllLauncherIconPackages(): List<ConfigApp> {
        val launcherIconPackageList = ArrayList<ConfigApp>()
        val pm = context.packageManager
        val installedPackages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES)
        for (info in installedPackages) {
            if (info.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM > 0) continue
            launcherIconPackageList.add(
                ConfigApp(
                    packageName = info.packageName,
                    appName = info.applicationInfo.loadLabel(pm).toString(),
                    icon = info.applicationInfo.loadIcon(pm),
                )
            )
        }
        return launcherIconPackageList.sortedBy { it.appName }
    }

    private suspend fun fetchAllCustomRule(): List<CustomRule> {
        val url =
            "https://gh.api.mystery0.app/https://raw.githubusercontent.com/Snoopy1866/LiTiaotiao-Custom-Rules/main/AllRules.json"
        val body = withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).execute()
            response.body?.string()
        }
        if (body.isNullOrBlank()) {
            Log.w(TAG, "fetchAllCustomRule: body is empty")
            return emptyList()
        }
        val resultList = ArrayList<CustomRule>(1024)
        withContext(Dispatchers.Default) {
            val moshi: Moshi = Moshi.Builder().build()
            val adapter = moshi.adapter<List<Map<String, String>>>(
                Types.newParameterizedType(
                    List::class.java,
                    Types.newParameterizedType(
                        Map::class.java,
                        String::class.java,
                        String::class.java,
                    ),
                )
            )
            val actionAdapter = moshi.adapter(RuleItem::class.java).lenient()
            val map = adapter.fromJson(body)
            map?.forEach {
                val nodeId = it.keys.first()
                val ruleItemJson = it.getValue(nodeId)
                val ruleItem = actionAdapter.fromJson(ruleItemJson)
                ruleItem?.popupRules?.forEach rules@{ action ->
                    if (action == null) return@rules
                    resultList.add(
                        CustomRule(
                            nodeId = nodeId,
                            actionId = action.id,
                            actionText = action.action,
                        )
                    )
                }
            }
        }
        return resultList
    }

    fun clearRuleMessage() {
        _ruleState.value = RuleState()
    }
}

data class AppState(
    val refreshing: Boolean = false,
    val list: List<ConfigApp> = emptyList(),
    val message: String = "",
)

data class ConfigApp(
    val packageName: String,
    val appName: String,
    val icon: Drawable,
)

data class RecordState(
    val refreshing: Boolean = false,
    val list: List<RecordApp> = emptyList(),
    val message: String = "",
)

data class RecordApp(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
    val triggerCount: Int,
)

data class RuleState(
    val refreshing: Boolean = false,
    val message: String = "",
)

@JsonClass(generateAdapter = true)
data class RuleItem(
    @Json(name = "popup_rules")
    val popupRules: List<Action?>,
)

@JsonClass(generateAdapter = true)
data class Action(
    val id: String,
    val action: String,
)