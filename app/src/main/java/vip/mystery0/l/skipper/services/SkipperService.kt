package vip.mystery0.l.skipper.services

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import vip.mystery0.l.skipper.model.FlowEventBus
import vip.mystery0.l.skipper.model.RunningRule

class SkipperService : AccessibilityService() {
    @OptIn(DelicateCoroutinesApi::class)
    private val scope = CoroutineScope(newFixedThreadPoolContext(5, "SkipperL"))

    companion object {
        private const val TAG = "SkipperService"
        const val EVENT_KEY = "accessibilityServiceEnabled"
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

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i(TAG, "onServiceConnected: service connected")
        scope.launch {
            FlowEventBus.post(EVENT_KEY, true)
        }
    }

    override fun onDestroy() {
        scope.launch {
            FlowEventBus.post(EVENT_KEY, false)
        }
        super.onDestroy()
        Log.i(TAG, "onDestroy: service destroyed")
    }
}