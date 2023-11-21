package com.apero.realistic.classes.adapter

import android.app.Activity
import android.graphics.Point
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemMyProjectBinding
import com.apero.realistic.model.ArtworkModel
import com.apero.realistic.widget.mainlist.ItemSizeProvider
import com.apero.realistic.widget.mainlist.ItemViewAdjuster
import eightbitlab.com.blurview.RenderScriptBlur

class MyProjectsAdapter(
    val context: Activity,
    var itemSizeProvider: ItemSizeProvider? = null,
    var itemViewAdjuster: ItemViewAdjuster? = null,
    val listenerCallback: OnItemClickListener<ArtworkModel>? = null
) : BaseAdapter<ArtworkModel, ItemMyProjectBinding>(listenerCallback) {
    private val itemSize: Point = Point()

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_my_project
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemMyProjectBinding) {
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
        item: ArtworkModel,
        position: Int,
        viewType: Int,
        binding: ItemMyProjectBinding
    ) {
        itemSizeProvider?.let {
            it.provideItemSize(itemSize, binding.root, position, ratio = item.ratio, activity = context)
            binding.root.layoutParams.width = itemSize.x
            binding.root.layoutParams.height = itemSize.y
        }
        itemViewAdjuster?.adjustListItemViewOnBind(binding.root, position)
    }
}