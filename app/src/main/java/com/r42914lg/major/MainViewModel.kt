package com.r42914lg.major

import android.app.Application
import androidx.compose.runtime.Stable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.r42914lg.major.data.ListFileStorage
import com.r42914lg.major.model.Car
import com.r42914lg.major.model.Visitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ScreenEvent {
    data object AddVisitor : ScreenEvent
    data class EditVisitor(val visitor: Visitor) : ScreenEvent
    data class RemoveVisitor(val visitor: Visitor) : ScreenEvent
}

@Stable
interface MainStateHolder {
    val screenState: StateFlow<List<Visitor>>
    fun onScreenEvent(event: ScreenEvent)
}

class MainViewModel(application: Application) : AndroidViewModel(application), MainStateHolder {
    private val dataStore = ListFileStorage(context = application)
    private val _screenState = MutableStateFlow<List<Visitor>>(emptyList())
    override val screenState: StateFlow<List<Visitor>>
        get() = _screenState.asStateFlow()

    init {
        viewModelScope.launch {
            _screenState.update { dataStore.read() }
        }
    }

    override fun onScreenEvent(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.EditVisitor -> {
                val current = _screenState.value.firstOrNull { it.id == event.visitor.id }
                current?.let { visitor ->
                    visitor.name = event.visitor.name
                    visitor.cars = event.visitor.cars.filter { it.make.isNotBlank() }
                    _screenState.update { _screenState.value.toList() }
                    viewModelScope.launch {
                        dataStore.write(_screenState.value)
                    }
                }
            }
            is ScreenEvent.RemoveVisitor -> {
                val list = _screenState.value.toMutableList()
                list.remove(event.visitor)
                _screenState.update { list }
                viewModelScope.launch {
                    dataStore.write(_screenState.value)
                }
            }
            is ScreenEvent.AddVisitor -> {
                val list = _screenState.value
                _screenState.update { list + Visitor() }
            }
        }
    }
}