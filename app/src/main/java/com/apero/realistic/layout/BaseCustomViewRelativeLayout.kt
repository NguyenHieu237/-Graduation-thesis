package com.apero.realistic.layout

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout

abstract class BaseCustomViewRelativeLayout : RelativeLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let {
            initStyleable(it)
        }
        setLayout()
        initView()
        initData()
        initListener()
    }

    protected open val styleableId: IntArray?
        get() = null

    protected open fun initDataFromStyleable(a: TypedArray) {}

    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()

    private fun initStyleable(attrs: AttributeSet) {
        if (styleableId != null && styleableId!!.isNotEmpty()) {
            val a = context.theme.obtainStyledAttributes(attrs, styleableId!!, 0, 0)
            initDataFromStyleable(a)
            a.recycle()
        }
    }

    private fun setLayout() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(layoutId, this, true)
    }
}