package team.redrock.stickerset.main.mvi

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch
import team.redrock.rain.lib.common.config.ui.mvvm.BaseVmBindFragment

abstract class MVIFragment<VM, VB, VE, VS> : BaseVmBindFragment<VM, VB>(), MVIPage<VE, VS>
        where VM : MVIViewModel<*, VE, VS>,
              VB : ViewBinding,
              VE : ViewEvent,
              VS : ViewState  {
    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        lifecycleScope.launch {
            viewModel.viewState.launchEffects()
        }
        viewModel.viewEvents.collectLaunch { renderViewEvent(it) }
    }
}