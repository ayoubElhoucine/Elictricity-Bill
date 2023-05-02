package com.ayoub.electricitybill.ui.bill.draft

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.extension.createImageUri
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.ui.theme.Purple500
import com.ayoub.electricitybill.ui.theme.Purple700
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
                (data.data as? Bill)?.let { bill ->
                    Success(viewModel = viewModel, bill = bill)
                }
            }
            else -> LaunchedEffect(Unit) { onBack() }
        }
    }
}

@Composable
private fun Success(
    viewModel: DraftBillViewModel,
    bill: Bill,
) {
    val conUiState = viewModel.conUiState.collectAsState()
    val myConsumption = viewModel.myConsumption.collectAsState()
    Column {
        when(conUiState.value) {
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                myConsumption.value?.let {
                    Text(it.toString())
                } ?: run {
                    NewConsumptionView(viewModel = viewModel)
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun NewConsumptionView(
    viewModel: DraftBillViewModel,
){
    val createUiState = viewModel.createUiState.collectAsState()
    val uploadImageUiState = viewModel.uploadImageUiState.collectAsState()
    val context = LocalContext.current
    val counter = remember { mutableStateOf("") }

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) {
        if (it) {
            cameraImageUri.value?.let { path ->
                viewModel.uploadConsumptionImage(path)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 40.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Button(
            modifier = Modifier.size(90.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple700),
            onClick = {
                cameraImageUri.value = context.createImageUri()
                cameraImageUri.value?.let {
                    cameraLauncher.launch(it)
                }
            },
        ) {
            when(uploadImageUiState.value) {
                UiState.Loading -> CircularProgressIndicator(color = Color.White)
                is UiState.Success -> Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.White,
                )
                is UiState.Fail -> Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = Color.White,
                )
                else -> Icon(
                    modifier = Modifier.size(50.dp),
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        }
        OutlinedTextField(
            value = counter.value,
            placeholder = {
                Text(text = "Compteur")
            },
            onValueChange = {
                counter.value = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )
        Button(
            modifier = Modifier.height(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Purple500
            ),
            onClick = {
                cameraImageUri.value?.let {
                    viewModel.createNewConsumption(counter.value.toDouble())
                } ?: run {
                    Toast.makeText(context, "Ajouter l'image de compteur", Toast.LENGTH_LONG).show()
                }
            },
            enabled = counter.value.isNotBlank(),
            shape = CircleShape
        ) {
            when(createUiState.value) {
                UiState.Idle, is UiState.Success -> Text(text = "Confirmer", color = Color.White)
                UiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.White)
                else -> Text(text = "Réessayez", color = Color.White)
            }
        }
    }
}