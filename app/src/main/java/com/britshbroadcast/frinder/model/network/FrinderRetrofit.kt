package com.britshbroadcast.frinder.model.network

import com.britshbroadcast.frinder.model.data.CafeResult
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class FrinderRetrofit {

    private val service: Service

    init {
        service = createService(createRetrofit())
    }

    private fun createRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com")   .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

}


    private fun createService(retrofit: Retrofit): Service = retrofit.create(Service::class.java)


    fun getNearByCafes(location: String, radius: String, type: String): Single<CafeResult> = service.getNearByCafes(
        location,
        radius,
        type,
        "AIzaSyA5rJh9FeteViW7n9M3iORS7d8L7W75wVI"
    )

}