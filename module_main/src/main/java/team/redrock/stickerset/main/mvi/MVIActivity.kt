package team.redrock.stickerset.main.mvi

import android.os.Bundle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import team.redrock.rain.lib.common.ui.mvvm.BaseVmBindActivity
import kotlin.reflect.KProperty1

abstract class MVIActivity<VM, VB, VE, VS> : BaseVmBindActivity<VM, VB>(), MVIPage<VE, VS>
        where VM : MVIViewModel<*, VE, VS>,
              VB : ViewBinding,
              VE : ViewEvent,
              VS : ViewState {

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        viewModel.viewState.launchEffects()
        viewModel.viewEvents.collectLaunch { renderViewEvent(it) }
    }

    // react hooks 风格
    protected fun <T, A> StateFlow<T>.useEffect(
        prop1: KProperty1<T, A>,
        action: (A) -> Unit
    ) {
        lifecycleScope.launch {
            this@useEffect.flowWithLifecycle(lifecycle).map {
                MVIFragment.StateTuple1(prop1.get(it))
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
                MVIFragment.StateTuple2(prop1.get(it), prop2.get(it))
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
                MVIFragment.StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
            }.collect { (a, b, c) ->
                action.invoke(a, b, c)
            }
        }
    }
}