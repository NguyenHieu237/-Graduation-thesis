package com.apero.realistic.classes.adapter

import android.content.Context
import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.setBackgroundTint
import com.apero.realistic.base.ext.setColorFilter
import com.apero.realistic.base.ext.setTextColor
import com.apero.realistic.databinding.ItemLanguageBinding
import com.apero.realistic.databinding.ItemRatioBinding
import com.apero.realistic.model.RatioModel
import kotlin.math.roundToInt

class RatioAdapter(
    val context: Context,
    val listenerCallback: OnItemClickListener<RatioModel>? = null
) : BaseAdapter<RatioModel, ItemRatioBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_ratio
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemRatioBinding) {

    }

    override fun bindData(
        item: RatioModel,
        position: Int,
        viewType: Int,
        binding: ItemRatioBinding
    ) {
        val itemIsSelected = item.ratioIsSelected()

        binding.lnContainer.setBackgroundTint(
            context,
            if (itemIsSelected) R.color.color_C5FEEA else R.color.color_25262A
        )

        binding.imgRatio.setColorFilter(
            context,
            if (itemIsSelected) R.color.color_010101 else R.color.color_777777
        )
        binding.imgRatio.layoutParams.width = (item.getMode().getRatioNumber().first * 2.5).roundToInt()
        binding.imgRatio.layoutParams.height = (item.getMode().getRatioNumber().second * 2.5).roundToInt()
        binding.imgRatio.requestLayout()

        binding.tvRatio.text = item.getRatioText()
        binding.tvRatio.setTextColor(
            context,
            if (itemIsSelected) R.color.color_010101 else R.color.color_777777
        )
    }
}