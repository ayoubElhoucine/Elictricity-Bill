package com.ayoub.electricitybill.ui.splash

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.ayoub.electricitybill.firebase.FirebaseUserAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val application: Application,
    private val auth: FirebaseUserAuth,
): ViewModel() {
    private val context: Context get() = application.applicationContext

    fun getStarted(
        onLogin: () -> Unit,
        onHome: () -> Unit,
    ) {
        if (auth.getUser() == null) onLogin()
        else onHome()
    }

}