package com.apero.realistic.classes.adapter

import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemRatioBinding
import com.apero.realistic.model.SettingModel
import com.apero.realistic.databinding.ItemSettingBinding

class SettingAdapter(
    val listenerCallback: OnItemClickListener<SettingModel>? = null
) : BaseAdapter<SettingModel,ItemSettingBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_setting
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemSettingBinding) {

    }

    override fun bindData(
        item: SettingModel,
        position: Int,
        viewType: Int,
        binding: ItemSettingBinding
    ) {
        binding.icon.setImageResource(item.icon)
        binding.name.text = item.name
    }
}