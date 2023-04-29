package com.ayoub.electricitybill.ui.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewBill: () -> Unit,
    onBillDetails: () -> Unit,
) {

}