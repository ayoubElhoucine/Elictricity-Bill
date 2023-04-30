package com.ayoub.electricitybill.ui.login

import android.app.Application
import android.content.Context
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.firebase.FirebaseUserAuth
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val firebase: FirebaseUserAuth,
): BaseViewModel<UiState>() {
    private val context: Context get() = application.applicationContext

    init {
        _uiState.value = UiState.Idle
    }

    fun login(
        email: String,
        password: String,
    ) {
        _uiState.value = UiState.Loading
        firebase.login(
            email, password,
            onSuccess = {
                _uiState.value = UiState.Success()
            },
            onFail =  {
                _uiState.value = UiState.Fail()
            }
        )
    }

}