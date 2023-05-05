package com.ayoub.electricitybill.ui.bill.newBill

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.R
import com.ayoub.electricitybill.extension.createImageUri
import com.ayoub.electricitybill.ui.theme.Purple500
import com.ayoub.electricitybill.ui.theme.Purple700
import com.ayoub.electricitybill.ui.uiState.UiState
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NewBillScreen(
    viewModel: NewBillViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val uploadImageUiState = viewModel.uploadImageUiState.collectAsState()
    val amount = remember { mutableStateOf("") }
    val extra = remember { mutableStateOf("") }

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

    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        tint = Color.White,
                        contentDescription = null,
                    )
                }
                Text(
                    text = "Nouvelle facture",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = 40.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Button(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(width = 2.dp, color = Purple500),
                onClick = {
                    cameraImageUri.value = context.createImageUri()
                    cameraImageUri.value?.let {
                        cameraLauncher.launch(it)
                    }
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                when (val data = uploadImageUiState.value) {
                    UiState.Loading -> CircularProgressIndicator(strokeWidth = 2.dp)
                    is UiState.Success -> {
                        (data.data as? String)?.let {
                            GlideImage(
                                modifier = Modifier.fillMaxSize(),
                                imageModel = it,
                                contentScale = ContentScale.FillBounds,
                                contentDescription = null,
                                loading = {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        CircularProgressIndicator(modifier = Modifier.align(
                                            Alignment.Center
                                        ), strokeWidth = 2.dp)
                                    }
                                }
                            )
                        } ?: run {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                tint = Color.White,
                            )
                        }
                    }
                    is UiState.Fail -> Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = Color.White,
                    )
                    else -> Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        tint = Purple500,
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount.value,
                placeholder = {
                    Text(text = "Montante de facutre")
                },
                onValueChange = {
                    amount.value = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Decimal
                ),
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = extra.value,
                placeholder = {
                    Text(text = "Extra")
                },
                onValueChange = {
                    extra.value = it
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
                    viewModel.createDraftBill(amount = amount.value.toDouble(), extra = extra.value.toDouble())
                },
                enabled = amount.value.isNotBlank() && extra.value.isNotBlank(),
                shape = RoundedCornerShape(8.dp),
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    when(uiState.value) {
                        UiState.Idle -> Text(text = "Confirmer", color = Color.White)
                        UiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(30.dp), color = Color.White)
                        is UiState.Success -> onBack()
                        else -> Text(text = "Réessayez", color = Color.White)
                    }
                }
            }
        }
    }
}