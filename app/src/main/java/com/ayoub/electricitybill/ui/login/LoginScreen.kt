package com.ayoub.electricitybill.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onHome: () -> Unit,
) {
    Box {
        Text(
            text = "login screen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}