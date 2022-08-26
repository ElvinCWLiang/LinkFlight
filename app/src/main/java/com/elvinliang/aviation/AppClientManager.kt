package com.elvinliang.aviation

import com.elvinliang.aviation.Config.API_KEY
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AppClientManager() {
    private val retrofitOpenSkyNetwork: Retrofit
    private val retrofitFlightAware: Retrofit

    init {
        retrofitOpenSkyNetwork = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientOpenSkyNetwork())
            .baseUrl(Config.URL_OpenSkyNetwork)
            .build()

        retrofitFlightAware = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientFlightAware())
            .baseUrl(Config.URL_FlightAware)
            .build()
    }

    private fun clientOpenSkyNetwork(): OkHttpClient? {
        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun clientFlightAware(): OkHttpClient? {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("x-apiKey", API_KEY)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    companion object {
        private val manager = AppClientManager()
        val client_OpenSkyNetwork: Retrofit
            get() = manager.retrofitOpenSkyNetwork
        val client_FlightAware: Retrofit
            get() = manager.retrofitFlightAware
    }
}