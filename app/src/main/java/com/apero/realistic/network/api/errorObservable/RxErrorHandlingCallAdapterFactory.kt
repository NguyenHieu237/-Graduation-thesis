package com.apero.realistic.network.api.errorObservable

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import com.apero.realistic.network.api.core.ApiConstant.HttpStatusCode.Companion.BAD_REQUEST
import com.apero.realistic.network.api.core.ApiConstant.HttpStatusCode.Companion.UNPROCESSABLE
import com.apero.realistic.network.api.core.ApiConstant.HttpStatusCode.Companion.CUSTOM_AUTH
import retrofit2.HttpException
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RxErrorHandlingCallAdapterFactory: CallAdapter.Factory() {
    private val _original by lazy {
        RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
    }

    companion object {
        fun create() : CallAdapter.Factory = RxErrorHandlingCallAdapterFactory()
    }

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *> {
        val wrapped = _original.get(returnType, annotations, retrofit) as CallAdapter<out Any, *>
        return RxCallAdapterWrapper(retrofit, wrapped)
    }

    private class RxCallAdapterWrapper<R>(val _retrofit: Retrofit,
                                          val _wrappedCallAdapter: CallAdapter<R, *>
    ): CallAdapter<R, Observable<R>> {

        override fun responseType(): Type = _wrappedCallAdapter.responseType()

        override fun adapt(call: Call<R>): Observable<R> {
            return (_wrappedCallAdapter.adapt(call) as? Observable<R>)?.onErrorResumeNext { throwable: Throwable ->
                Observable.error(asRetrofitException(throwable))
            } ?: Observable.empty<R>()
        }

        private fun asRetrofitException(throwable: Throwable): RetrofitException {
            // Run in main thread
//            Handler(Looper.getMainLooper()).post {
//                // Do not show popup when have not network because exist class handle it
//                // Please check ServerErrorObserver file to check error network
//                if (throwable !is IOException && (throwable as? HttpException)?.response()?.code() != UNPROCESSABLE)
//                    ToastUtil.show(BaseApplication.getInstance().baseApplication.getString(com.pch.base.R.string.default_error), BaseApplication.getInstance().baseApplication.applicationContext)
//            }

            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response()
                return if (throwable.code() == UNPROCESSABLE || throwable.code() == BAD_REQUEST || throwable.code() == CUSTOM_AUTH) {
                    // on out api 422's get metadata in the response. Adjust logic here based on your needs
                    RetrofitException.httpErrorWithObject(  url = response.raw().request().url().toString(),
                                                            response = response,
                                                            retrofit = _retrofit
                    )
                } else {
                    RetrofitException.httpError(url = response.raw().request().url().toString(),
                                                response = response,
                                                retrofit = _retrofit
                    )
                }
            }

            // A network error happened
            if (throwable is IOException) {
                return RetrofitException.networkError(throwable)
            }

            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.unexpectedError(throwable)
        }
    }
}