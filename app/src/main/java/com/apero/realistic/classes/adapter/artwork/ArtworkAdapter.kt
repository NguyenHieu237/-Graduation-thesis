package com.apero.realistic.classes.adapter.artwork

import android.app.Activity
import android.graphics.Point
import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemArtworkBinding
import com.apero.realistic.model.ArtworkModel
import com.apero.realistic.widget.mainlist.ItemSizeProvider
import com.apero.realistic.widget.mainlist.ItemViewAdjuster

class ArtworkAdapter(
    val context: Activity,
    var itemSizeProvider: ItemSizeProvider? = null,
    var itemViewAdjuster: ItemViewAdjuster? = null,
    val listenerCallback: OnItemClickListener<ArtworkModel>? = null
) : BaseAdapter<ArtworkModel, ItemArtworkBinding>(listenerCallback) {
    private val itemSize: Point = Point()

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_artwork
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemArtworkBinding) {
        binding.viewDownload.activeBlurView(context = context, viewGroup = parent)
        binding.viewLike.setIcon(context = context, iconId = R.drawable.ic_heart)
        binding.viewLike.activeBlurView(context = context, viewGroup = parent)
    }

    override fun bindData(
        item: ArtworkModel,
        position: Int,
        viewType: Int,
        binding: ItemArtworkBinding
    ) {
        itemSizeProvider?.let {
            it.provideItemSize(itemSize, binding.root, position, ratio = item.ratio, activity = context)
            binding.root.layoutParams.width = itemSize.x
            binding.root.layoutParams.height = itemSize.y
        }
        itemViewAdjuster?.adjustListItemViewOnBind(binding.root, position)
    }
}