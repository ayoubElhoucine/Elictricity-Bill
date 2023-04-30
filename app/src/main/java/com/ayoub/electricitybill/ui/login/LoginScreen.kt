package com.ayoub.electricitybill.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.ui.theme.Purple500
import com.ayoub.electricitybill.ui.uiState.UiState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onHome: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    val name = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "Connexion",
            fontSize = 26.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black,
        )
        OutlinedTextField(
            value = name.value,
            placeholder = {
                Text(text = "Nom d'utilisateur")
            },
            onValueChange = {
                name.value = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            )
        )
        OutlinedTextField(
            value = password.value,
            placeholder = {
                Text(text = "Mote de pass")
            },
            onValueChange = {
                password.value = it
            }
        )
        Button(
            modifier = Modifier.height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Purple500
            ),
            onClick = {
                viewModel.login(name.value, password.value)
            },
            enabled = name.value.isNotBlank() && password.value.isNotBlank(),
            shape = CircleShape
        ) {
            when(uiState.value) {
                UiState.Idle -> Text(text = "Continue", color = Color.White)
                UiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.White)
                is UiState.Success -> {
                    LaunchedEffect(Unit) {
                        onHome()
                    }
                }
                else -> Text(text = "Réessayez", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}