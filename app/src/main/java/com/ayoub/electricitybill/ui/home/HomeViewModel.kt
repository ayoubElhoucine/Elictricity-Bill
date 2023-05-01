package com.ayoub.electricitybill.ui.home

import android.app.Application
import android.content.Context
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.firebase.FirebaseDatabase
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val firebaseDatabase: FirebaseDatabase,
): BaseViewModel<UiState>() {

    init {
        getBills()
    }

    private fun getBills() {
        _uiState.value = UiState.Loading
        firebaseDatabase.getBills(
            onSuccess = {
                _uiState.value = UiState.Success(data = it)
            },
            onFail = {
                _uiState.value = UiState.Fail()
            }
        )
    }

    fun getDraftBill(onBillDetails: () -> Unit) {
        firebaseDatabase.getDraftBill {
            onBillDetails()
        }
    }
}