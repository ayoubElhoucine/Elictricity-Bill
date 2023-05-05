package com.ayoub.electricitybill.model

data class Consumer(
    val id: String = "",
    val role: String = "user",
    val name: String = "",
    val phone: String = "",
    val token: String = "",
) {
    val isAdmin: Boolean get() = role == "admin"
}
