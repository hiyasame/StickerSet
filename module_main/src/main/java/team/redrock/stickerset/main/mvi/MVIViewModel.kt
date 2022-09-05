package team.redrock.stickerset.main.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MVIViewModel<VA: ViewAction, VE: ViewEvent, VS: ViewState> : ViewModel() {
    protected abstract val mutableViewState: MutableStateFlow<VS>
    protected abstract val mutableViewEvents: MutableSharedFlow<VE>

    /**
     * viewEvents
     */
    val viewEvents: SharedFlow<VE>
        get() = mutableViewEvents

    /**
     * viewState
     */
    val viewState: StateFlow<VS>
        get() = mutableViewState

    /**
     * 发布ViewAction
     *
     * @param action
     */
    abstract fun dispatch(action: VA)
}