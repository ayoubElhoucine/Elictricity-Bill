package com.ayoub.electricitybill.ui.bill.draft

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.ui.uiState.UiState

@Composable
fun DraftBillScreen(
    viewModel: DraftBillViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ){
        when(val data = uiState.value) {
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                Text(data.data.toString())
            }
            else -> LaunchedEffect(Unit) { onBack() }
        }
    }
}

@Composable
private fun Success(
    bill: Bill
) {
    Column {

    }
}