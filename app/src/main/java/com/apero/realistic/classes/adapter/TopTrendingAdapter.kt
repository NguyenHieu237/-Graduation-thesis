package com.apero.realistic.classes.adapter

import android.content.Context
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemTrendingBinding
import com.apero.realistic.model.TrendingModel
import eightbitlab.com.blurview.RenderScriptBlur

class TopTrendingAdapter(
    val context: Context,
    val listenerCallback: OnItemClickListener<TrendingModel>? = null
) : BaseAdapter<TrendingModel, ItemTrendingBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_trending
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemTrendingBinding) {
        binding.viewDownload.activeBlurView(context = context, viewGroup = parent)
        binding.viewLike.setIcon(context = context, iconId = R.drawable.ic_heart)
        binding.viewLike.activeBlurView(context = context, viewGroup = parent)
    }

    override fun bindData(
        item: TrendingModel,
        position: Int,
        viewType: Int,
        binding: ItemTrendingBinding
    ) {

    }
}