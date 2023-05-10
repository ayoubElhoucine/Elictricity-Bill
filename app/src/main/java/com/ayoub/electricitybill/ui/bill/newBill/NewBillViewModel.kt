package com.ayoub.electricitybill.ui.bill.newBill

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.data.firebase.FirebaseDatabase
import com.ayoub.electricitybill.data.repo.ApiRepo
import com.ayoub.electricitybill.data.request.BodyRequest
import com.ayoub.electricitybill.data.request.DataRequest
import com.ayoub.electricitybill.data.request.NotificationRequest
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.model.Consumer
import com.ayoub.electricitybill.ui.uiState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject


@HiltViewModel
class NewBillViewModel @Inject constructor(
    private val application: Application,
    private val firebaseDatabase: FirebaseDatabase,
    private val repo: ApiRepo,
): BaseViewModel<UiState>() {

    private val _uploadImageUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val uploadImageUiState: StateFlow<UiState> get() = _uploadImageUiState

    val context: Context get () = application.applicationContext
    private var billImage: String? = null

    init {
        _uiState.value = UiState.Idle
    }

    fun createDraftBill(amount: Double, extra: Double) {
        billImage?.let { image ->
            _uiState.value = UiState.Loading
            val bill = Bill(
                id = UUID.randomUUID().toString(),
                amount = amount,
                name = "${amount}DA",
                extra = extra,
                image = image,
                createdAt = System.currentTimeMillis()
            )
            firebaseDatabase.createDraftBill(
                bill = bill,
                onSuccess = {
                    firebaseDatabase.getConsumers { cos ->
                        viewModelScope.launch(Dispatchers.IO) {
                            cos.forEach {
                                repo.pushNotification(
                                    body = BodyRequest(
                                        to = it.token,
                                        notification = NotificationRequest(
                                            title = "Nouvelle facture 🚨",
                                            body = "Nouvelle facture arrivée 👀🚨💡"
                                        ),
                                        data = DataRequest(),
                                    )
                                )
                                delay(100)
                            }
                        }
                    }
                },
                onFail = {
                    _uiState.value = UiState.Fail()
                }
            )
        }

    }

    fun uploadBillImage(uri: Uri) {
        _uploadImageUiState.value = UiState.Loading
        firebaseDatabase.uploadBillImage(uri) { image ->
            billImage = image
            image?.let {
                _uploadImageUiState.value = UiState.Success(it)
            } ?: run {
                _uploadImageUiState.value = UiState.Fail()
            }
        }
    }
}