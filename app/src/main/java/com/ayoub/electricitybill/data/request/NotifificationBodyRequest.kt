package com.ayoub.electricitybill.data.request

import com.squareup.moshi.Json

data class BodyRequest(
    @field:Json(name = "to") val to: String,
    @field:Json(name = "notification") val notification: NotificationRequest,
)

data class NotificationRequest(
    @field:Json(name = "body") val body: String = "body",
    @field:Json(name = "OrganizationId") val organizationId: String = "2",
    @field:Json(name = "content_available") val contentAvailable: Boolean = true,
    @field:Json(name = "priority") val priority: String = "high",
    @field:Json(name = "subtitle") val subtitle: String = "subtitle",
    @field:Json(name = "title") val title: String = "title",
)
