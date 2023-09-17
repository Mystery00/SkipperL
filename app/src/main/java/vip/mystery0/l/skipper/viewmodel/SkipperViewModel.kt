package vip.mystery0.l.skipper.viewmodel

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vip.mystery0.l.skipper.context
import vip.mystery0.l.skipper.model.RunningRule
import vip.mystery0.l.skipper.store.SkipperStore

class SkipperViewModel : ViewModel() {
    //全局禁用开关
    private val _globalEnable = MutableStateFlow(RunningRule.enable)
    val globalEnable: StateFlow<Boolean> = _globalEnable

    //全局关键词
    val globalKeywords = mutableStateListOf<String>()

    //所有应用列表
    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState

    //禁用应用列表
    val disabledAppList = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            globalKeywords.addAll(SkipperStore.globalKeywords)
            disabledAppList.addAll(SkipperStore.disabledAppList)
        }
        refreshAppList()
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
            _appState.value = AppState(message = throwable.message ?: "未知错误")
        }) {
            _appState.value = AppState(refreshing = true)
            _appState.value = AppState(list = getAllLauncherIconPackages())
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