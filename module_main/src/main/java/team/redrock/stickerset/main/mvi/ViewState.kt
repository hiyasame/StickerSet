package team.redrock.stickerset.main.mvi

import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

interface ViewState : Cloneable {
    public override fun clone(): Any {
        return super.clone()
    }
}