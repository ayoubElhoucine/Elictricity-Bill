package com.ayoub.electricitybill.model

data class Consumption(
    val id: String,
    val prevValue: Double,
    val currValue: Double,
    val value: Double,
    val image: String,
    val consumer: String,
    val createdAt: Double,
)