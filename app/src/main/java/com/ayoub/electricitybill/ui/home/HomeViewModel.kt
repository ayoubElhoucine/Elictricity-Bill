package com.ayoub.electricitybill.ui.home

import android.app.Application
import android.content.Context
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
): BaseViewModel<UiState>() {
    val context: Context get () = application.applicationContext
}