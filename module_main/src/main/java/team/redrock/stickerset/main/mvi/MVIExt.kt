package team.redrock.stickerset.main.mvi

import kotlinx.coroutines.flow.*
import kotlin.reflect.KProperty1

inline fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
    this.value = this.value.reducer()
}