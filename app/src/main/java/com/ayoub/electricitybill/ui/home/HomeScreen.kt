package com.ayoub.electricitybill.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ayoub.electricitybill.model.Bill
import com.ayoub.electricitybill.ui.theme.Purple700
import com.ayoub.electricitybill.ui.uiState.UiState
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNewBill: () -> Unit,
    onDraftedBill: () -> Unit,
    onBillDetails: (Bill) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getDraftBill(onDraftedBill)
    }
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Factures",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = Purple700,
                onClick = onNewBill,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        backgroundColor = Color.LightGray.copy(0.3f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (val data = uiState.value) {
                UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is UiState.Success -> {
                    (data.data as? List<Bill>)?.let {
                        Success(bills = it, onBillDetails = onBillDetails)
                    } ?: run {
                        Text("Aucun résultat")
                    }
                }
                else -> Text("Aucun résultat")
            }
        }
    }
}

@Composable
private fun Success(
    bills: List<Bill>,
    onBillDetails: (Bill) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 20.dp)
    ) {
        itemsIndexed(bills) { _, item ->
            BillItem(bill = item) {
                onBillDetails(item)
            }
        }
    }
}

@Composable
private fun BillItem(
    bill: Bill,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                imageModel = bill.image,
                loading = {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray.copy(0.5f))
                    )
                }
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = bill.name,
                    color = Color.Black,
                    fontWeight = FontWeight.W600,
                    fontSize = 18.sp
                )
                Text(
                    text = bill.createdAt.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp
                )
            }
        }
    }
}