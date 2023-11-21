package com.apero.realistic.network.api.core

import com.apero.realistic.network.action.manager.SessionManager
import com.apero.realistic.widget.tag.LogUtil
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class RequestTimeOutInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code() == ApiConstant.HttpStatusCode.BAD_GATEWAY_SERVER) {

        }
        return response
    }
}

class NetworkInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response
    }
}

class HeaderInterceptor: Interceptor {
//    /**
//     * Init header for request
//     */

    override fun intercept(chain: Interceptor.Chain): Response {
        val headers = hashMapOf("Accept" to "application/json",
                                "Content-Type" to "application/json",
                                "device" to "android",
                                "App-name" to "AIGeneratorConfiguration.shared.getAppName()",
                                ApiConstant.RequestParamKey.AUTHORIZATION_HEADER to SessionManager.instance.getTokenAuth())

        val requestBuilder = chain.request().newBuilder()

        // TODO add parameter for header
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }

        LogUtil.i("HeaderString: ", headers)

        return chain.proceed(requestBuilder.build())
    }
}

object LoggingInterceptor {
    val instance: HttpLoggingInterceptor
        get() {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            return logging
        }
}