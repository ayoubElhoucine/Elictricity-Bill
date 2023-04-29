package com.ayoub.electricitybill.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onHome: () -> Unit,
    onLogin: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getStarted(
            onLogin = onLogin,
            onHome = onHome,
        )
    }
    Box {
        Text(
            text = "splash screen",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}