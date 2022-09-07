package team.redrock.stickerset.main.mvi

import kotlinx.coroutines.flow.*
import kotlin.reflect.KProperty1

// distinctUntilChanged 防抖，不过StateFlow似乎自带
// react hooks 风格
suspend fun <T, A> StateFlow<T>.useEffect(
    prop1: KProperty1<T, A>,
    action: (A) -> Unit
) {
    this.map {
        StateTuple1(prop1.get(it))
    }.distinctUntilChanged().collect { (a) ->
        action.invoke(a)
    }
}

suspend fun <T, A, B> StateFlow<T>.useEffect(
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    action: (A, B) -> Unit
) {
    this.map {
        StateTuple2(prop1.get(it), prop2.get(it))
    }.distinctUntilChanged().collect { (a, b) ->
        action.invoke(a, b)
    }
}

suspend fun <T, A, B, C> StateFlow<T>.useEffect(
    prop1: KProperty1<T, A>,
    prop2: KProperty1<T, B>,
    prop3: KProperty1<T, C>,
    action: (A, B, C) -> Unit
) {
    this.map {
        StateTuple3(prop1.get(it), prop2.get(it), prop3.get(it))
    }.distinctUntilChanged().collect { (a, b, c) ->
        action.invoke(a, b, c)
    }
}

internal data class StateTuple1<A>(val a: A)
internal data class StateTuple2<A, B>(val a: A, val b: B)
internal data class StateTuple3<A, B, C>(val a: A, val b: B, val c: C)

inline fun <T> MutableStateFlow<T>.setState(reducer: T.() -> T) {
    this.value = this.value.reducer()
}