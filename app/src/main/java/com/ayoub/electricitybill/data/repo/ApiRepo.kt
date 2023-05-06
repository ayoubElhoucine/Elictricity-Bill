package com.ayoub.electricitybill.data.repo

import com.ayoub.electricitybill.data.network.Api
import com.ayoub.electricitybill.data.network.SafeApiCall
import com.ayoub.electricitybill.data.request.BodyRequest
import javax.inject.Inject

class ApiRepo @Inject constructor(
    private val api: Api
) : SafeApiCall {

    suspend fun pushNotification(body: BodyRequest) = safeApiCall { api.pushNotification(body) }

}