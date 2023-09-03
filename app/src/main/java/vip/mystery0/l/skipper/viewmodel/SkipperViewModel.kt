package vip.mystery0.l.skipper.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vip.mystery0.l.skipper.model.RunningRule
import vip.mystery0.l.skipper.store.SkipperStore

class SkipperViewModel : ViewModel() {
    //全局禁用开关
    private val _globalEnable = MutableStateFlow(RunningRule.enable)
    val globalEnable: StateFlow<Boolean> = _globalEnable

    //全局关键词
    val globalKeywords = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            globalKeywords.addAll(SkipperStore.globalKeywords)
        }
    }

    fun changeGlobalEnable(enable: Boolean) {
        viewModelScope.launch {
            SkipperStore.enable = enable
            RunningRule.enable = enable
            _globalEnable.value = enable
        }
    }

    fun updateGlobalKeywords(keywords: Set<String>) {
        SkipperStore.globalKeywords = keywords
        RunningRule.globalKeywords.clear()
        RunningRule.globalKeywords.addAll(keywords)
        globalKeywords.clear()
        globalKeywords.addAll(keywords)
    }
}