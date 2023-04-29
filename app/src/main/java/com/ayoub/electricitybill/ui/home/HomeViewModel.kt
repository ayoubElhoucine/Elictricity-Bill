package com.ayoub.electricitybill.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
): ViewModel() {
    val context: Context get () = application.applicationContext
}