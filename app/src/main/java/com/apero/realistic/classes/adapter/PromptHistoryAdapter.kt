package com.apero.realistic.classes.adapter

import android.content.Context
import android.view.ViewGroup
import com.apero.realistic.R
import com.apero.realistic.base.adapter.BaseAdapter
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.databinding.ItemPromptHistoryBinding
import com.apero.realistic.model.PromptHistoryModel

class PromptHistoryAdapter(
    val context: Context,
    val listenerCallback: OnItemClickListener<PromptHistoryModel>? = null
) : BaseAdapter<PromptHistoryModel, ItemPromptHistoryBinding>(listenerCallback) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_prompt_history
    }

    override fun setupBinding(parent: ViewGroup, binding: ItemPromptHistoryBinding) {

    }

    override fun bindData(
        item: PromptHistoryModel,
        position: Int,
        viewType: Int,
        binding: ItemPromptHistoryBinding
    ) {

    }
}