package team.redrock.stickerset.main.mvi

import android.os.Bundle
import android.view.View
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.redrock.rain.lib.common.ui.mvvm.BaseVmBindFragment
import kotlin.reflect.KProperty1

abstract class MVIFragment<VM, VB, VE, VS> : BaseVmBindFragment<VM, VB>(), MVIPage<VE, VS>
        where VM : MVIViewModel<*, VE, VS>,
              VB : ViewBinding,
              VE : ViewEvent,
              VS : ViewState  {

    private var eventsCollectJob: Job? = null

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        viewModel.viewState.launchEffects()
        // 取消之前的订阅，避免重复订阅
        eventsCollectJob?.cancel()
        eventsCollectJob = viewModel.viewEvents.collectLaunch { renderViewEvent(it) }
    }

    // react hooks 风格
    protected fun <T, A> StateFlow<T>.useEffect(
        prop1: KProperty1<T, A>,
        action: (A) -> Unit
    ) {
        lifecycleScope.launch {
            this@useEffect.flowWithLifecycle(lifecycle).map {
                StateTuple1(prop1.get(it))
            }.collect { (a) ->
                action.invoke(a)
            }
        }
    }

    protected fun <T, A, B> StateFlow<T>.useEffect(
        prop1: KProperty1<T, A>,
        prop2: KProperty1<T, B>,
        action: (A, B) -> Unit
    ) {
        lifecycleScope.launch {
            this@useEffect.flowWithLifecycle(lifecycle).map {
                StateTuple2(prop1.get(it), prop2.get(it))
            }.collect { (a, b) ->
                action.invoke(a, b)
            }
        }
    }

    protected fun <T, A, B, C> StateFlow<T>.useEffect(
        prop1: KProperty1<T, A>,
        prop2: KProperty1<T, B>,
        prop3: KProperty1<T, C>,
        action: (A, B, C) -> Unit
    ) {
        lifecycleScope.launch {
            this@useEffect.flowWithLifecycle(lifecycle).map {
                StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
            }.collect { (a, b, c) ->
                action.invoke(a, b, c)
            }
        }
    }

    internal data class StateTuple1<A>(val a: A)
    internal data class StateTuple2<A, B>(val a: A, val b: B)
    internal data class StateTuple3<A, B, C>(val a: A, val b: B, val c: C)
}