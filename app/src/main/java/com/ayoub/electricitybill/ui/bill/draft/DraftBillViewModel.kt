package com.ayoub.electricitybill.ui.bill.draft

import android.net.Uri
import com.ayoub.electricitybill.base.BaseViewModel
import com.ayoub.electricitybill.firebase.FirebaseDatabase
import com.ayoub.electricitybill.firebase.FirebaseUserAuth
import com.ayoub.electricitybill.firebase.billsRef
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.model.Consumption
import com.ayoub.electricitybill.ui.uiState.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DraftBillViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseUserAuth,
): BaseViewModel<UiState>() {

    private val _uploadImageUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val uploadImageUiState: StateFlow<UiState> get() = _uploadImageUiState

    private val _createUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val createUiState: StateFlow<UiState> get() = _createUiState

    private val _conUiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)
    val conUiState: StateFlow<UiState> get() = _conUiState

    private val _myConsumption: MutableStateFlow<Consumption?> = MutableStateFlow(null)
    val myConsumption: StateFlow<Consumption?> get() = _myConsumption

    private var consumptionImage: String? = null

    init {
        getData()
    }

    private fun getData() {
        _uiState.value = UiState.Loading
        firebaseDatabase.getDraftBill {
            _uiState.value = UiState.Success(it)
            getBillConsumptions(it.id)
        }
    }

    private fun getBillConsumptions(id: String) {
        _conUiState.value = UiState.Loading
        firebaseDatabase.getBillConsumptions(
            id = id,
            onSuccess = {
                _conUiState.value = UiState.Success(it)
                firebaseAuth.getUser()?.let { user ->
                    _myConsumption.value = it.find { e -> e.consumer == user.uid }
                }
            },
            onFail = {
                _conUiState.value = UiState.Fail()
            }
        )
    }

    fun createNewConsumption(counter: Double, missingImage: () -> Unit){
        (_uiState.value as? UiState.Success)?.let { data ->
            (data.data as? Bill)?.let {bill ->
                _createUiState.value = UiState.Loading
                consumptionImage?.let { image ->
                    firebaseAuth.getUser()?.let { user ->
                        firebaseDatabase.createNewConsumption(
                            consumption = Consumption(
                                id = UUID.randomUUID().toString(),
                                currCounter = counter,
                                image = image,
                                consumer = user.uid,
                                bill = bill.id,
                                createdAt = System.currentTimeMillis()
                            ),
                            onSuccess = {
                                _createUiState.value = UiState.Success()
                                getBillConsumptions(id = bill.id)
                            },
                            onFail = {
                                _createUiState.value = UiState.Fail()
                            }
                        )
                    } ?: run {
                        _createUiState.value = UiState.Fail()
                    }
                } ?: run {
                    _createUiState.value = UiState.Fail()
                    missingImage()
                }
            }
        }
    }

    fun uploadConsumptionImage(uri: Uri) {
        _uploadImageUiState.value = UiState.Loading
        firebaseDatabase.uploadConsumptionImage(uri) { image ->
            consumptionImage = image
            image?.let {
                _uploadImageUiState.value = UiState.Success()
            } ?: run {
                _uploadImageUiState.value = UiState.Fail()
            }
        }
    }
}