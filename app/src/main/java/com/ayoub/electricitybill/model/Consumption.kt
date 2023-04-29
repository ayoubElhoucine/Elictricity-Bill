package com.ayoub.electricitybill.model

data class Consumption(
    val id: String,
    val value: Double,
    val image: String,
    val consumerId: String,
    val billId: String,
    val createdAt: Double
)