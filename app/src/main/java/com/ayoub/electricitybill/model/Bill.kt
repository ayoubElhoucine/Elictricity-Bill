package com.ayoub.electricitybill.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bill(
    val id: String = "",
    val amount: Double = 0.0,
    val name: String = "",
    val image: String = "",
    val extra: Double = 0.0,
    val createdAt: Long = 0,
): Parcelable
