package com.apero.realistic.base.ext

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.jakewharton.rxbinding2.internal.VoidToUnit
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit

/**
 * Created by Pham Cong Hoan on 2019-12-14.
 */

fun <T> Observable<T>.ignoreFastTap(): Observable<T> {
    return throttleFirst(1000, TimeUnit.MILLISECONDS)
}

fun <T> Observable<T>.ignoreFastSearch(): Observable<T> {
    return debounce(1000, TimeUnit.MILLISECONDS)
}

fun Disposable.disposedBy(bag: CompositeDisposable): Boolean = bag.add(this)

fun <T: Any> Observable<T>.bindTo(subject: Subject<T>): Disposable {
    return this.subscribe { value -> subject.onNext(value) }
}

fun View.click(): Observable<Unit> = RxView.clicks(this).map(VoidToUnit)

inline fun <T> T.guardElse(valueReturn: () -> Boolean): T {
    if (this == false) valueReturn.invoke()
    return this
}

inline fun <T: Any?> T.guardLet(valueReturn: T.() -> Unit): T {
    if (this == null) valueReturn()
    return this!!
}

fun TextView.rxTextChange(): Observable<String> {
    return Observable.create<String> { emitter ->
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                s?.toString()?.let { emitter.onNext(it) }
            }
        }
        this.addTextChangedListener(textWatcher)
        emitter.setCancellable {
            this.removeTextChangedListener(textWatcher)
        }
    }
}
