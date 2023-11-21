package com.apero.realistic.classes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.goneIf
import com.apero.realistic.base.ext.hiddenIf
import com.apero.realistic.base.ext.setTextColor
import com.apero.realistic.databinding.ItemTagCategoryBinding
import com.apero.realistic.model.TagCategoryModel

class TagCategoryAdapter(
    val context: Context,
    val listenerCallback: OnItemClickListener<TagCategoryModel>? = null
) : BaseAdapter<TagCategoryModel, ItemTagCategoryBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_tag_category
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemTagCategoryBinding) {
    }

    @SuppressLint("SetTextI18n")
    override fun bindData(
        item: TagCategoryModel,
        position: Int,
        viewType: Int,
        binding: ItemTagCategoryBinding
    ) {
        binding.apply {
            viewSelected.hiddenIf { !item.selected }
            tvTagCategorySelected.hiddenIf { !item.selected }
            tvTagCategorySelected.text = item.getTitlePrefix()
            tvTagCategoryUnSelected.hiddenIf { item.selected }
            tvTagCategoryUnSelected.text = item.getTitlePrefix()
        }
    }
}