package com.ayoub.electricitybill.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
): ViewModel() {
    private val context: Context get() = application.applicationContext
}