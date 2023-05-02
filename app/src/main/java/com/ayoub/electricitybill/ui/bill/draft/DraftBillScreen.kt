package com.ayoub.electricitybill.ui.bill.draft

import android.net.Uri
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.extension.createImageUri
import com.ayoub.electricitybill.extension.toNiceFormat
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.model.Consumption
import com.ayoub.electricitybill.ui.theme.Purple500
import com.ayoub.electricitybill.ui.theme.Purple700
import com.ayoub.electricitybill.ui.uiState.UiState
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DraftBillScreen(
    viewModel: DraftBillViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Nouvelle facture",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            when (val data = uiState.value) {
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
}

@Composable
private fun Success(
    viewModel: DraftBillViewModel,
    bill: Bill,
) {
    val conUiState = viewModel.conUiState.collectAsState()
    val myConsumption = viewModel.myConsumption.collectAsState()
    Column {
        when (val data = conUiState.value) {
            UiState.Loading -> Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                ) {
                    item {
                        myConsumption.value?.let {
                            ConsumptionDetailsView(it, bill.extra)
                        } ?: run {
                            NewConsumptionView(viewModel = viewModel)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        BillInfoView(bill = bill, consumptions = data.data as? List<Consumption>)
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun NewConsumptionView(
    viewModel: DraftBillViewModel,
) {
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

    Card(
        modifier = Modifier.padding(16.dp),
        elevation = 10.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                text = "Information sur votre consomation",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
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
                when (uploadImageUiState.value) {
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
                    viewModel.createNewConsumption(counter.value.toDouble()) {
                        Toast.makeText(context, "Ajouter l'image de compteur", Toast.LENGTH_LONG)
                            .show()
                    }
                },
                enabled = counter.value.isNotBlank(),
                shape = CircleShape
            ) {
                when (createUiState.value) {
                    UiState.Idle, is UiState.Success -> Text(
                        text = "Confirmer",
                        color = Color.White
                    )
                    UiState.Loading -> CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color.White
                    )
                    else -> Text(text = "Réessayez", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ConsumptionDetailsView(
    consumption: Consumption,
    extra: Double?,
) {
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "Details de votre consomation",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
            consumption.prevCounter?.let {
                ConsumptionDetailsViewItem(
                    title = "Ancien compteur",
                    value = it.toInt().toString()
                )
            }
            consumption.currCounter.let {
                ConsumptionDetailsViewItem(
                    title = "Nouveau compteur",
                    value = it.toInt().toString()
                )
            }
            consumption.value?.let {
                ConsumptionDetailsViewItem(
                    title = "Consommation",
                    value = it.toInt().toString()
                )
            }
            consumption.cost?.let {
                Box(
                    modifier = Modifier
                        .border(width = 1.dp, shape = RoundedCornerShape(8.dp), color = Color.Gray)
                        .background(color = Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    ConsumptionDetailsViewItem(
                        title = "Prix:",
                        value = "${it.toNiceFormat()}DA"
                    )
                }
            }
            extra?.let { gaz ->
                ConsumptionDetailsViewItem(
                    title = "Prix de gaz:",
                    value = "${gaz.toNiceFormat()}DA"
                )
                consumption.cost?.let {
                    Box(
                        modifier = Modifier
                            .background(
                                shape = RoundedCornerShape(8.dp),
                                color = if (consumption.payed) Purple700 else Color.Red
                            )
                            .padding(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = "Totale:",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                            Text(
                                text = "${(it + gaz).toNiceFormat()}DA",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W800
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BillInfoView(
    bill: Bill,
    consumptions: List<Consumption>?,
) {
    Card(
        elevation = 10.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = "Details de facture",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
            )
            ConsumptionDetailsViewItem(
                title = "Facture",
                value = bill.name
            )
            GlideImage(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                imageModel = bill.image,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

@Composable
private fun ConsumptionDetailsViewItem(
    title: String,
    value: String,
    color: Color = Color.Black,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = color,
            fontSize = 16.sp,
        )
        Text(
            text = value,
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600
        )
    }
}