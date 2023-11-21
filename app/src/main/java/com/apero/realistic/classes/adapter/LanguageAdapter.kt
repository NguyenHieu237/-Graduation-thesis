package com.apero.realistic.classes.adapter

import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.databinding.ItemLanguageBinding
import com.apero.realistic.model.LanguageModel

class LanguageAdapter : BaseAdapter<LanguageModel, ItemLanguageBinding>() {
    private var onItemClickListener: ((LanguageModel) -> Unit)? = null
    private var prevPos = -1

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_language
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemLanguageBinding) {
        // do nothing
    }

    fun setOnItemClickListener(listener: (LanguageModel) -> Unit) {
        this.onItemClickListener = listener
    }

    override fun bindData(item: LanguageModel, position: Int, viewType: Int, binding: ItemLanguageBinding) {
        binding.apply {

            root.setOnClickListener {
                if (prevPos > -1 && prevPos < listItems.size) {
                    listItems[prevPos].isSelected = false
                    notifyItemChanged(prevPos)
                }
                item.isSelected = true

                notifyItemChanged(position)
                prevPos = position
                onItemClickListener?.invoke(item)
            }

            imgLanguage.setImageResource(item.imgRes)
            name.setText(item.nameRes)
            checkbox.setImageResource(
                if (item.isSelected) R.drawable.ic_radio_checked
                else R.drawable.ic_radio_unchecked
            )
            container.setBackgroundResource(
                if (item.isSelected) R.drawable.bg_language_item_selected
                else R.drawable.bg_language_item
            )
        }
    }

    override fun setListItems(list: ArrayList<LanguageModel>) {
        prevPos = list.indexOf(list.firstOrNull { language -> language.isSelected } )
        super.setListItems(list)
    }
}
