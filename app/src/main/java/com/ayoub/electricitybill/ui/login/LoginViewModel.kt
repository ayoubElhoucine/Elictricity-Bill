package com.ayoub.electricitybill.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
): BaseViewModel<UiState>() {
    private val context: Context get() = application.applicationContext

    init {
        _uiState.value = UiState.Idle
    }

    fun login(
        username: String,
        password: String,
    ) {

    }

}