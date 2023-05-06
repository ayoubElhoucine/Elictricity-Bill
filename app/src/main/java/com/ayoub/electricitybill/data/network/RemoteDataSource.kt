package com.ayoub.electricitybill.data.network

import android.app.Application
import com.ayoub.electricitybill.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    application: Application,
) {
    private val baseUrl: String = "https://fcm.googleapis.com/"

    fun <Api> buildApi(api: Class<Api>): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getRetrofitClient())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
            .create(api)
    }

    private fun getRetrofitClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also { builder ->
                    builder.addHeader("Content-Type", "application/json")
                    builder.addHeader("Authorization", "key=AAAA08FW22U:APA91bFqsSDyXtd6eVLXGYxtAi04cCSxSl_1m18gcc9wJnP_2tTV9QkgjPikxqoQkgKpPNyJZj3K8YwJmcYe62dLNkYlE1kgEUkCarX6dGiFnFOHrjWnRiW9dbFS_9D8uw7OYx9dtAG8")
                }.build())
            }.also { client ->
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                } else {
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.NONE)
                    client.addInterceptor(logging)
                }
            }.build()
    }
}