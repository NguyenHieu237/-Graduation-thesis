package com.apero.realistic.layout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.apero.realistic.R

class RealisticBottomTabLayout: BaseBottomTabLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var mListImage: ArrayList<ImageView>
    private lateinit var mListText: ArrayList<TextView>

    override val layoutId: Int
        get() = R.layout.layout_base_bottom_tab

    override fun initView() {
        val tabDiscovery: LinearLayout = findViewById(R.id.tabDiscovery)
        val tabGenerate: LinearLayout = findViewById(R.id.tabGenerate)

        mTabs = ArrayList()
        mTabs.add(tabDiscovery)
        mTabs.add(tabGenerate)

        mListImage = ArrayList()
        mListImage.add(findViewById(R.id.imgDiscovery))
        mListImage.add(findViewById(R.id.imgGenerate))

        mListText = ArrayList()
        mListText.add(findViewById(R.id.tvDiscovery))
        mListText.add(findViewById(R.id.tvGenerate))
    }

    override fun initData() {

    }

    override fun initListener() {
        for ((position, tab) in mTabs.withIndex())  {
            tab.setOnClickListener { onClickTab(position) }
        }
    }

    private fun onClickTab(position: Int) {
        if (mCurrentTab == position) {
            mOnTabSelectedListener?.onTabReselected(position)
        } else {
            mCurrentTab = position
            mOnTabSelectedListener?.onTabSelect(position)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun activeTab(tabIndex: Int) {
        mCurrentTab = tabIndex
        mListImage[0].setImageDrawable(context.resources.getDrawable(if (tabIndex == 0) R.drawable.ic_discovery_active else R.drawable.ic_discovery_un_active, null))
        mListImage[1].setImageDrawable(context.resources.getDrawable(if (tabIndex == 1) R.drawable.ic_generate_active else R.drawable.ic_generate_un_active, null))

        mListText.forEachIndexed { index, textView ->
            textView.setTextColor(context.resources.getColor(if (index == tabIndex) R.color.color_F7CDC1 else R.color.color_555555))
        }
    }
}