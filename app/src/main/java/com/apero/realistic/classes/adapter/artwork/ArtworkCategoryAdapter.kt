package com.apero.realistic.classes.adapter.artwork

import android.content.Context
import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.setBackgroundTint
import com.apero.realistic.base.ext.setTextColor
import com.apero.realistic.databinding.ItemArtworkCategoryBinding
import com.apero.realistic.model.ArtworkCategoryModel

class ArtworkCategoryAdapter(
    val context: Context,
    val listenerCallback: OnItemClickListener<ArtworkCategoryModel>? = null
) : BaseAdapter<ArtworkCategoryModel, ItemArtworkCategoryBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_artwork_category
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemArtworkCategoryBinding) {

    }

    override fun bindData(
        item: ArtworkCategoryModel,
        position: Int,
        viewType: Int,
        binding: ItemArtworkCategoryBinding
    ) {
        val itemIsSelected = item.selected

        binding.lnContainer.setBackgroundTint(
            context,
            if (itemIsSelected) R.color.color_C5FEEA else R.color.color_25262A
        )

        binding.tvCategory.text = item.category
        binding.tvCategory.setTextColor(
            context,
            if (itemIsSelected) R.color.color_010101 else R.color.color_999999
        )
    }
}