package com.ayoub.electricitybill.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayoub.electricitybill.extension.toNiceFormat
import com.ayoub.electricitybill.model.Consumer
import com.ayoub.electricitybill.model.Consumption
import com.ayoub.electricitybill.ui.bill.draft.DraftBillViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun TileListItem(
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