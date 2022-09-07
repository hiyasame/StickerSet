package team.redrock.stickerset.main.mvi

import android.util.Property
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty1

abstract class MVIViewModel<VA: ViewAction, VE: ViewEvent, VS: ViewState> : ViewModel() {
    protected abstract val mutableViewState: MutableStateFlow<VS>
    protected val mutableViewEvents: MutableSharedFlow<VE> = MutableSharedFlow()

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
    abstract suspend fun dispatch(action: VA)

    protected inline fun setState(reducer: VS.() -> VS) {
        mutableViewState.value = mutableViewState.value.reducer()
    }
}