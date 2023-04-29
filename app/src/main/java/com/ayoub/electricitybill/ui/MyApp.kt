package com.ayoub.electricitybill.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.ayoub.electricitybill.ui.theme.ElectricityBillTheme

@Composable
fun MyApp(
    finishActivity: () -> Unit,
) {
    val useDarkIcons = MaterialTheme.colors.isLight
    val appState = rememberAppState()

    ElectricityBillTheme {
        AppNavGraph(
            appState = appState,
        )
    }
}