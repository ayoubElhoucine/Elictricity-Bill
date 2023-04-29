package com.ayoub.electricitybill.model

data class Bill(
    val id: String,
    val name: String,
    val image: String,
    val consumptions: List<String>,
    val createdAt: Double
)
