package com.ayoub.electricitybill.data.network

import com.ayoub.electricitybill.data.request.BodyRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {

    @POST("fcm/send")
    suspend fun pushNotification(@Body body: BodyRequest): Any?

}