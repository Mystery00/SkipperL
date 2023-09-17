package vip.mystery0.l.skipper.model

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object FlowEventBus {
    private val bus: HashMap<String, MutableSharedFlow<out Any>> = hashMapOf()

    private fun <T : Any> with(key: String): MutableSharedFlow<T> {
        @Suppress("unchecked_cast")
        return (bus[key] ?: MutableSharedFlow<T>().also { bus[key] = it }) as MutableSharedFlow<T>
    }

    /**
     * 对外只暴露SharedFlow
     */
    fun <T> getFlow(action: String): SharedFlow<T> {
        return with(action)
    }

    /**
     * 挂起函数
     */
    suspend fun <T : Any> post(action: String, data: T) {
        with<T>(action).emit(data)
    }

    /**
     * 详见tryEmit和emit的区别
     */
    fun <T : Any> tryPost(action: String, data: T): Boolean {
        return with<T>(action).tryEmit(data)
    }

    /**
     * sharedFlow会长久持有，所以要加声明周期限定，不然会出现内存溢出
     */
    fun <T : Any> subscribe(
        lifecycleOwner: LifecycleOwner,
        action: String,
        block: (T) -> Unit
    ) {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            with<T>(action).collect {
                lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    block(it)
                }
            }
        }
    }
}