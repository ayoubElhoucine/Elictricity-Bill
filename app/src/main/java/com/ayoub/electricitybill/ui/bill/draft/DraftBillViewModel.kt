package com.ayoub.electricitybill.ui.bill.draft

import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.firebase.FirebaseDatabase
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DraftBillViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
): BaseViewModel<UiState>() {

    init {
        getData()
    }

    private fun getData() {
        _uiState.value = UiState.Loading
        firebaseDatabase.getDraftBill {
            _uiState.value = UiState.Success(it)
        }
    }
}