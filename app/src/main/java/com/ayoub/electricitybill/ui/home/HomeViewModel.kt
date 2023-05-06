package com.ayoub.electricitybill.ui.home

import androidx.lifecycle.viewModelScope
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.data.firebase.FirebaseDatabase
import com.ayoub.electricitybill.data.firebase.FirebaseUserAuth
import com.ayoub.electricitybill.data.network.Api
import com.ayoub.electricitybill.data.repo.ApiRepo
import com.ayoub.electricitybill.data.request.BodyRequest
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
                    to = "eT3McPoZR-ecZVAusj0FoB:APA91bGJutJyEqb3DZrgP-WV81UukALuS1nQAS4QHBq8J3nq17AdsY0387yUORbh9rjWwWjTHF3al8KY9ud5lobuJurqgQ_LLMlP901Kq8714C09lzJyuj3B0DucVEKXcZf1xjHnooTy",
                    notification = NotificationRequest()
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