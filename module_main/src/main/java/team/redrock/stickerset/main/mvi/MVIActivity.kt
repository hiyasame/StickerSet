package team.redrock.stickerset.main.mvi

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import team.redrock.rain.lib.common.config.ui.mvvm.BaseVmBindActivity

abstract class MVIActivity<VM, VB, VE, VS> : BaseVmBindActivity<VM, VB>()
        where VM : MVIViewModel<*, VE, VS>,
              VB : ViewBinding,
              VE : ViewEvent,
              VS : ViewState {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        lifecycleScope.launch {
            viewModel.viewState.launchEffect()
        }
        viewModel.viewEvents.collectLaunch { renderViewEvent(it) }
    }

    /**
     * 初始化View
     */
    abstract fun initView()

    /**
     * 注册Effect
     * 在里面使用useEffect
     */
    abstract suspend fun StateFlow<VS>.launchEffect()

    /**.
     * 渲染ViewEvent
     *
     * @param viewEvent 事件
     */
    abstract suspend fun renderViewEvent(viewEvent: VE)
}