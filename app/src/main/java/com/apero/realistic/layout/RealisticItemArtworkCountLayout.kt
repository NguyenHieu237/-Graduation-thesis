package com.apero.realistic.layout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import com.apero.realistic.R
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

class RealisticItemArtworkCountLayout: BaseCustomViewLinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val layoutId: Int
        get() = R.layout.item_artwork_count

    override fun initView() {

    }

    override fun initData() {
    }

    override fun initListener() {
    }

    private fun getBlurView(): BlurView {
        return findViewById(R.id.blurView)
    }

    fun activeBlurView(context: Context, viewGroup: ViewGroup) {
        getBlurView().apply {
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true

            setupWith(viewGroup)
                .setBlurAlgorithm(RenderScriptBlur(context))
                .setBlurRadius(20F)
                .setHasFixedTransformationMatrix(false)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setIcon(context: Context, iconId: Int) {
        val icon = findViewById<ImageView>(R.id.imgIcon)
        icon.setImageDrawable(context.resources.getDrawable(iconId, null))
    }
}