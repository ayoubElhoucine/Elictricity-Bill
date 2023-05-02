package com.ayoub.electricitybill.model

data class Consumption(
    val id: String = "",
    val prevCounter: Double? = null,
    val currCounter: Double = 0.0,
    val value: Double? = null,
    val cost: Double? = null,
    val image: String = "",
    val consumer: String = "",
    val bill: String = "",
    val createdAt: Long = 0,
)