package com.apero.realistic.base

import androidx.lifecycle.ViewModel
import com.apero.realistic.utils.Event
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class BaseViewModel: ViewModel() {
    protected val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    protected val progress: BehaviorSubject<Event<Boolean>> = BehaviorSubject.create()
    val makeProgress : Observable<Event<Boolean>>
        get() = progress.observeOn(AndroidSchedulers.mainThread())

    override fun onCleared() {
        super.onCleared()
        bag.clear()
        bag.dispose()
    }
}