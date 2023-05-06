package com.ayoub.electricitybill.ui.bill.details

import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.data.firebase.FirebaseDatabase
import com.ayoub.electricitybill.model.Consumer
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BillDetailsViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
): BaseViewModel<UiState>() {

    fun getBillConsumptions(id: String) {
        _uiState.value = UiState.Loading
        firebaseDatabase.getBillConsumptions(
            id = id,
            onSuccess = {
                _uiState.value = UiState.Success(it)
            },
            onFail = {
                _uiState.value = UiState.Fail()
            }
        )
    }

    fun getConsumerById(id: String, onComplete: (Consumer?) -> Unit) {
        firebaseDatabase.getConsumerById(id, onComplete = onComplete)
    }

    fun togglePayed(id: String, value: Boolean, onSuccess: () -> Unit) {
        firebaseDatabase.toggleConsumptionPayed(
            id, value,
            onSuccess = onSuccess,
        )
    }

}