package com.apero.realistic.classes.bottomsheet

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseBottomSheetDialogFragment
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.PromptHistoryAdapter
import com.apero.realistic.databinding.BottomSheetPromptHistoryBinding
import com.apero.realistic.model.PromptHistoryModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PromptHistoryBottomSheet: BaseBottomSheetDialogFragment<BottomSheetPromptHistoryBinding>() {
    private var adapter: PromptHistoryAdapter? = null

    override val layoutId: Int
        get() = R.layout.bottom_sheet_prompt_history

    override fun setupUI() {
        super.setupUI()

        dialog?.setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.isHideable = false
            BottomSheetBehavior.from(binding.containerRoot.parent as View).peekHeight = binding.containerRoot.height/2
            bottomSheetBehavior.peekHeight = binding.containerRoot.height
            binding.containerRoot.parent.requestLayout()
        }

        this.setupAdapter()
    }

    private fun setupAdapter() {
        val context = context.guardLet { return }!!
        val list: ArrayList<PromptHistoryModel> = arrayListOf(
            PromptHistoryModel(),
            PromptHistoryModel(),
            PromptHistoryModel(),
            PromptHistoryModel(),
            PromptHistoryModel(),
            PromptHistoryModel(),
            PromptHistoryModel(),
            PromptHistoryModel()
        )
        adapter = PromptHistoryAdapter(context = context, listenerCallback = object :
            OnItemClickListener<PromptHistoryModel> {
            override fun onItemClick(position: Int, model: PromptHistoryModel) {

            }
        })
        val manager = LinearLayoutManager(context)
        binding.rcvPromptHistory.layoutManager = manager
        binding.rcvPromptHistory.adapter = adapter
        adapter?.setListItems(list = list)
    }
}