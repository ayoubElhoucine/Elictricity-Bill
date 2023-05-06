package com.ayoub.electricitybill.ui.bill.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.extension.toNiceFormat
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.model.Consumer
import com.ayoub.electricitybill.model.Consumption
import com.ayoub.electricitybill.ui.common.LocalAdminRole
import com.ayoub.electricitybill.ui.components.TileListItem
import com.ayoub.electricitybill.ui.uiState.UiState
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun BillDetailsScreen(
    viewModel: BillDetailsViewModel = hiltViewModel(),
    bill: Bill,
    onBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getBillConsumptions(bill.id)
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
                    text = "Details de facture",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            }
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    Spacer(modifier = Modifier.size(20.dp))
                    TileListItem(
                        title = "Facture",
                        value = bill.name
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    TileListItem(
                        title = "Prix de gaz (1):",
                        value = "${bill.extra.toNiceFormat()}DA"
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    GlideImage(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                        imageModel = bill.image,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    when(val data = uiState.value) {
                        is UiState.Success -> {
                            (data.data as? List<Consumption>)?.let { items ->
                                items.forEach { item ->
                                    ConsumptionItem(
                                        consumption = item,
                                        viewModel = viewModel,
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun ConsumptionItem(
    consumption: Consumption,
    viewModel: BillDetailsViewModel,
) {
    val isAdmin = LocalAdminRole.current
    val isPayed = remember { mutableStateOf(consumption.payed) }
    val consumer = remember { mutableStateOf<Consumer?>(null) }
    viewModel.getConsumerById(id = consumption.consumer) {
        consumer.value = it
    }

    Column(
        modifier = Modifier
            .background(Color.Black.copy(0.01f), shape = RoundedCornerShape(10.dp))
            .border(width = 1.dp, color = Color.Black.copy(0.2f), shape = RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = Color.LightGray,
            )
            consumer.value?.let {
                Text(
                    it.name,
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = Color.Black,
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 10.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isAdmin) {
            consumption.prevCounter?.let {
                TileListItem(
                    title = "Ancien compteur",
                    value = it.toInt().toString()
                )
            }
            consumption.currCounter.let {
                TileListItem(
                    title = "Nouveau compteur",
                    value = it.toInt().toString()
                )
            }
        }
        consumption.value?.let {
            TileListItem(
                title = "Consommation",
                value = it.toInt().toString()
            )
        }
        if (isAdmin) {
            consumption.cost?.let {
                TileListItem(
                    title = "Prix:",
                    value = "${it.toNiceFormat()}DA"
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Payé",
                    color = Color.Black,
                    fontSize = 16.sp,
                )
                Switch(checked = isPayed.value, onCheckedChange = {
                    viewModel.togglePayed(
                        id = consumption.id,
                        value = it,
                    ) {
                        isPayed.value = it
                    }
                })
            }
        }
    }
}