package com.elvinliang.di

import com.elvinliang.common.Constants
import com.elvinliang.common.Constants.URL_FlightAware
import com.elvinliang.common.Constants.URL_OpenSkyNetwork
import com.elvinliang.remote.FlightAwareService
import com.elvinliang.remote.OpenSkyNetworkService
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    @Named("OpenSkyNetworkClient")
    fun providesOpenSkyNetworkClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        AndroidFlipperClient.getInstanceIfInitialized()?.let { flipperClient ->
            okHttpClient.addNetworkInterceptor(
                FlipperOkhttpInterceptor(
                    flipperClient.getPlugin(
                        NetworkFlipperPlugin.ID
                    )
                )
            )
        }
        return okHttpClient.connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @Named("FlightAwareClient")
    fun providesFlightAwareClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        AndroidFlipperClient.getInstanceIfInitialized()?.let { flipperClient ->
            okHttpClient.addNetworkInterceptor(
                FlipperOkhttpInterceptor(
                    flipperClient.getPlugin(
                        NetworkFlipperPlugin.ID
                    )
                )
            )
        }
        return okHttpClient
            .addInterceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("x-apiKey", Constants.API_KEY)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @Named("provideOpenSkyNetworkRetrofit")
    fun provideOpenSkyNetworkRetrofit(
        @Named("OpenSkyNetworkClient") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(URL_OpenSkyNetwork)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    @Named("provideFlightAwareRetrofit")
    fun provideFlightAwareRetrofit(
        @Named("FlightAwareClient") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(URL_FlightAware)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideFlightAwareService(
        @Named("provideFlightAwareRetrofit") retrofit: Retrofit
    ): FlightAwareService = retrofit.create(FlightAwareService::class.java)

    @Singleton
    @Provides
    fun provideOpenSkyNetworkService(
        @Named("provideOpenSkyNetworkRetrofit") retrofit: Retrofit
    ): OpenSkyNetworkService = retrofit.create(OpenSkyNetworkService::class.java)
}