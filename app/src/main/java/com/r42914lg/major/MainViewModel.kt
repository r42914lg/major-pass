package com.r42914lg.major

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.r42914lg.major.model.Visitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ScreenEvent {
    data class AddVisitor(val visitor: Visitor) : ScreenEvent
    data class RemoveVisitor(val visitor: Visitor) : ScreenEvent
}

@Stable
interface MainStateHolder {
    val screenState: StateFlow<List<Visitor>?>
    fun onScreenEvent(event: ScreenEvent)
}

class MainViewModel : ViewModel(), MainStateHolder {
    private val _screenState = MutableStateFlow(null)
    override val screenState: StateFlow<List<Visitor>?>
        get() = _screenState.asStateFlow()

    override fun onScreenEvent(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.AddVisitor -> {}
            is ScreenEvent.RemoveVisitor -> {}
        }
    }
}