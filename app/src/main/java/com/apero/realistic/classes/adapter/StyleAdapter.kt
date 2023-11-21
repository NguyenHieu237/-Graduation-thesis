package com.apero.realistic.classes.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemStyleBinding
import com.apero.realistic.model.StyleModel
import eightbitlab.com.blurview.RenderScriptBlur

class StyleAdapter(
    val context: Context,
    val listenerCallback: OnItemClickListener<StyleModel>? = null
) : BaseAdapter<StyleModel, ItemStyleBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_style
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemStyleBinding) {
        binding.blurView.apply {
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true

            setupWith(parent)
                .setBlurAlgorithm(RenderScriptBlur(context))
                .setBlurRadius(11F)
                .setHasFixedTransformationMatrix(false)
        }
    }

    override fun bindData(
        item: StyleModel,
        position: Int,
        viewType: Int,
        binding: ItemStyleBinding
    ) {

    }
}