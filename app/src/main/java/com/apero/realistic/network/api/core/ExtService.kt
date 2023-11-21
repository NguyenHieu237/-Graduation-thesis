package com.apero.realistic.network.api.core

import com.apero.realistic.base.ext.disposedBy
import com.apero.realistic.network.api.errorObservable.RetrofitException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

inline fun <reified T> Observable<T>.subscribeBy(noinline onNext: ((T) -> Unit)? = null, noinline onError: ((RetrofitException) -> Unit)? = null, noinline onComplete: (() -> Any?)? = null, bag: CompositeDisposable? = null): Disposable {
    return this.subscribe({
        onNext?.invoke(it)
    }, {
        (it as? RetrofitException)?.let { it1 -> onError?.invoke(it1) }
    }, {
        onComplete?.invoke()
    }, {
        bag?.let { myBag ->
            it.disposedBy(myBag)
        }
    })
}

fun <T> Observable<T>.rxDefaultRequest(): Observable<T> {
    return this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//    return this.subscribeOn(Schedulers.io())
}