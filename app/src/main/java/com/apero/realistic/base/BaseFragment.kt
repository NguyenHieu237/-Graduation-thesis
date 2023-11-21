package com.apero.realistic.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        setupData()
        setupUI()
        setupListener()
        setupViewModel()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        bag.clear()
        bag.dispose()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: T
    protected val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    protected open fun setupData() {}
    protected abstract fun setupUI()
    protected abstract fun setupListener()
    protected abstract fun setupViewModel()
}