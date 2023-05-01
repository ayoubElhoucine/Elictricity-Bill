package com.ayoub.electricitybill.ui.bill.newBill

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.extension.createImageUri
import com.ayoub.electricitybill.ui.theme.Purple700
import com.ayoub.electricitybill.ui.uiState.UiState

@Composable
fun NewBillScreen(
    viewModel: NewBillViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val uploadImageUiState = viewModel.uploadImageUiState.collectAsState()

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) {
        if (it) {
            cameraImageUri.value?.let { path ->
                viewModel.uploadBillImage(path)
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
    }
}