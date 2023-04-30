package com.ayoub.electricitybill.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

abstract class BaseViewModel<T> : ViewModel() {

    @Inject
    lateinit var globalScope: CoroutineScope

    protected val _uiState: MutableStateFlow<T?> = MutableStateFlow(null)
    val uiState: StateFlow<T?> get() = _uiState

}