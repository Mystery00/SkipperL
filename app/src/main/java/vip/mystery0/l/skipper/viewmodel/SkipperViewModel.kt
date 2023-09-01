package vip.mystery0.l.skipper.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import vip.mystery0.l.skipper.model.RunningRule
import vip.mystery0.l.skipper.store.SkipperStore

class SkipperViewModel : ViewModel() {
    val globalKeywords = mutableStateListOf<String>()

    init {
        viewModelScope.launch {
            globalKeywords.addAll(SkipperStore.globalKeywords)
        }
    }

    fun updateGlobalKeywords(keywords: List<String>) {
        globalKeywords.clear()
        globalKeywords.addAll(keywords)
        RunningRule.globalKeywords.clear()
        RunningRule.globalKeywords.addAll(keywords)
    }
}