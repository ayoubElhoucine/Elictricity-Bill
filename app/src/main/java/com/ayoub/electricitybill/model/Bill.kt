package com.ayoub.electricitybill.model

data class Bill(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val extra: Double = 0.0,
    val consumptions: List<Consumption>? = null,
    val createdAt: Long = 0,
)
