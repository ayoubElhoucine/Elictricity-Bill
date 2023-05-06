package com.ayoub.electricitybill.ui

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import com.ayoub.electricitybill.ui.common.LocalAdminRole
import com.ayoub.electricitybill.ui.common.LocalCoroutineScope
import com.ayoub.electricitybill.ui.theme.ElectricityBillTheme

@Composable
fun MyApp(
    finishActivity: () -> Unit,
    isAdmin: MutableLiveData<Boolean>,
) {
    val appState = rememberAppState()
    val adminRole = isAdmin.observeAsState()

    ElectricityBillTheme {
        CompositionLocalProvider(
            LocalAdminRole provides (adminRole.value ?: false),
            LocalCoroutineScope provides appState.coroutineScope,
        ) {
            AppNavGraph(
                appState = appState,
            )
        }
    }
}