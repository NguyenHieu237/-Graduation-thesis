package com.apero.realistic.layout

import android.content.Context
import android.util.AttributeSet
import android.view.View

abstract class BaseBottomTabLayout: BaseCustomViewRelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        const val TAB_NOTIFICATION = 2
    }
    protected var mCurrentTab = 0
    protected var mOnTabSelectedListener: OnTabSelectedListener? = null
    protected lateinit var mTabs: ArrayList<View>

    fun setOnTabSelectedListener(listener: OnTabSelectedListener) {
        mOnTabSelectedListener = listener
    }
    fun showUnread(){}

    fun hideUnread() {}

    interface OnTabSelectedListener {
        fun onTabReselected(position: Int)
        fun onTabSelect(position: Int)
    }
}