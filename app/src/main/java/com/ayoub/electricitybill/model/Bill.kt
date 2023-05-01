package com.ayoub.electricitybill.model

data class Bill(
    val id: String,
    val name: String,
    val image: String,
    val extra: Double,
    val consumptions: List<Consumption>,
    val createdAt: Double,
)
