package com.apero.realistic.network.api.core

import com.apero.realistic.network.api.errorObservable.RxErrorHandlingCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Pham Cong Hoan on 16/01/2019.
 */

class ApiClient {
    companion object {
        private const val REQUEST_TIMEOUT: Long = 60

        private val okHttpClientBuilder: OkHttpClient.Builder by lazy {
            initOkHttpClientBuilder()
        }
        private val client by lazy {
            buildClient()
        }
        private val retrofit by lazy {
            buildRetrofit()
        }

        private fun initOkHttpClientBuilder(): OkHttpClient.Builder {
            /** Builder */
            return OkHttpClient().newBuilder()
                    .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(RequestTimeOutInterceptor())
                    .addInterceptor(NetworkInterceptor())
                    .addInterceptor(HeaderInterceptor())
                    .addInterceptor(LoggingInterceptor.instance)
        }

        private fun buildClient(): OkHttpClient {
            return okHttpClientBuilder.build()
        }

        private fun buildRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://www.google.com/")
                .client(client)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <T> createService(service: Class<T>): T {
            return retrofit.create(service)
        }

        fun build(): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}
