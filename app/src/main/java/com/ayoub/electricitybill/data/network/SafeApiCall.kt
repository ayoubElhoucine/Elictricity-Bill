package com.ayoub.electricitybill.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

interface SafeApiCall {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            false,
                            400,
                            throwable.response()?.message() ?: "somthing went wrong",
                            throwable.response()?.errorBody()
                        )
                    }
                    else -> {
                        Resource.Failure(true, 0, "net_work_exception", null)
                    }
                }
            }
        }
    }
}