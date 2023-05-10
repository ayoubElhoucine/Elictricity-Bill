package com.ayoub.electricitybill.ui.home

import android.provider.ContactsContract.Data
import androidx.lifecycle.viewModelScope
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.data.firebase.FirebaseDatabase
import com.ayoub.electricitybill.data.firebase.FirebaseUserAuth
import com.ayoub.electricitybill.data.network.Api
import com.ayoub.electricitybill.data.repo.ApiRepo
import com.ayoub.electricitybill.data.request.BodyRequest
import com.ayoub.electricitybill.data.request.DataRequest
import com.ayoub.electricitybill.data.request.NotificationRequest
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseUserAuth: FirebaseUserAuth,
    private val repo: ApiRepo,
): BaseViewModel<UiState>() {

    init {
        getBills()
        updateFcmToken()
        viewModelScope.launch {
            delay(1000)
            repo.pushNotification(
                BodyRequest(
                    to = "dmj7itRKQ3Sp59mC3NVr90:APA91bGpE9_jE0AtvjvLp-2a6-_o2CV7PCFrYoabN7xNbGk4py-aVxt7dGmAEeCg-QvqYtuzfgy9bMFnn1JcbXBA9cmopCH_4xbM8WORp6E_50z4-HCvAvi3CjyqlSfQsDYHLN1ZgywV",
                    notification = NotificationRequest(),
                    data = DataRequest()
                )
            )
        }
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

    private fun updateFcmToken() {
        firebaseUserAuth.getUser()?.let {
            firebaseDatabase.updateFcmToken(it.uid)
        }
    }

    fun getDraftBill(onBillDetails: () -> Unit) {
        firebaseDatabase.getDraftBill {
            onBillDetails()
        }
    }
}