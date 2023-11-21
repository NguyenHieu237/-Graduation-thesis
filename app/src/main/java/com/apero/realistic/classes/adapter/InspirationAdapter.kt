package com.apero.realistic.classes.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemInspirationBinding
import com.apero.realistic.model.InspirationModel
import eightbitlab.com.blurview.RenderScriptBlur

class InspirationAdapter(
    val context: Context,
    val onAddInspirationCompletion: OnItemClickListener<InspirationModel>? = null
) : BaseAdapter<InspirationModel, ItemInspirationBinding>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_inspiration
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemInspirationBinding) {
        binding.blurView.apply {
            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true

            setupWith(parent)
                .setBlurAlgorithm(RenderScriptBlur(context))
                .setBlurRadius(16F)
                .setHasFixedTransformationMatrix(false)
        }
    }

    override fun bindData(
        item: InspirationModel,
        position: Int,
        viewType: Int,
        binding: ItemInspirationBinding
    ) {
        binding.blurView.setOnClickListener {
            onAddInspirationCompletion?.onItemClick(position, item)
        }
    }
}