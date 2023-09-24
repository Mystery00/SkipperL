package vip.mystery0.l.skipper.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import org.greenrobot.eventbus.EventBus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import vip.mystery0.l.skipper.model.RunningRule
import vip.mystery0.l.skipper.store.RecordStore

class SkipperService : AccessibilityService(), KoinComponent {
    @OptIn(DelicateCoroutinesApi::class)
    private val scope = CoroutineScope(newFixedThreadPoolContext(5, "SkipperL"))

    private val eventBus: EventBus by inject()

    companion object {
        private const val TAG = "SkipperService"
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return
        if (!RunningRule.enable) return
        event?.let { e ->
            val packageName: String? = e.packageName?.toString()
            if (RunningRule.disabledAppList.contains(packageName)) return
            RunningRule.globalKeywords.forEach { keywords ->
                scope.launch {
                    searchNodeByText(rootNode, packageName, keywords)?.let {
                        if (!it.isClickable) return@launch
                        if (RunningRule.disabledAppList.contains(it.packageName)) return@launch
                        if (RunningRule.debug)
                            Log.d(TAG, "onAccessibilityEvent: ${it.packageName} -> ${it.text}")
                        it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        toast(RunningRule.interceptText)
                        RecordStore.saveRecord(it.packageName, it.text)
                    }
                }
            }
            if (!packageName.isNullOrBlank()) {
                RunningRule.rules[packageName.hashCode()]?.forEach { (key, value) ->
                    scope.launch {
                        if (searchNodeByText(rootNode, packageName, key) != null) {
                            searchNodeByText(rootNode, packageName, value)?.let {
                                if (!it.isClickable) return@launch
                                if (RunningRule.disabledAppList.contains(it.packageName)) return@launch
                                if (RunningRule.debug)
                                    Log.d(
                                        TAG,
                                        "onAccessibilityEvent: ${it.packageName} -> ${it.text}"
                                    )
                                it.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                                toast(RunningRule.interceptText)
                                RecordStore.saveRecord(it.packageName, it.text)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun searchNodeByText(
        rootNode: AccessibilityNodeInfo,
        packageName: String?,
        text: String
    ): AccessibilityNodeInfo? {
        rootNode.findAccessibilityNodeInfosByText(text).takeUnless { it.isNullOrEmpty() }
            ?.let { return it[0] }
        if (!packageName.isNullOrBlank()) {
            rootNode.findAccessibilityNodeInfosByViewId("$packageName:id/$text")
                .takeUnless { it.isNullOrEmpty() }
                ?.let { return it[0] }
        }
        return null
    }

    override fun onInterrupt() {
    }

    private fun toast(text: String) {
        //TODO 怎么显示Toast呢
//        scope.launch(Dispatchers.Main) {
//            Toast.makeText(context, text, Toast.LENGTH_SHORT)
//                .show()
//        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "onServiceConnected: service connected")
        eventBus.post(MessageEvent(true))
        scope.launch {
            RunningRule.loadRules()
        }
    }

    override fun onDestroy() {
        eventBus.post(MessageEvent(false))
        super.onDestroy()
        Log.i(TAG, "onDestroy: service destroyed")
    }
}

data class MessageEvent(val enable: Boolean)