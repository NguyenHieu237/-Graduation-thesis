package com.apero.realistic.classes.bottomsheet.editprompt

import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.apero.realistic.R
import com.apero.realistic.base.BaseBottomSheetDialogFragment
import com.apero.realistic.base.adapter.OnItemClickListener
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.classes.adapter.RatioAdapter
import com.apero.realistic.databinding.BottomSheetEditPromptBinding
import com.apero.realistic.model.RatioModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class EditPromptBottomSheet : BaseBottomSheetDialogFragment<BottomSheetEditPromptBinding>() {
    private var adapter: RatioAdapter? = null
    private val viewModel: EditPromptViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.bottom_sheet_edit_prompt

    override fun setupUI() {
        super.setupUI()

        dialog?.setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.isHideable = false
            BottomSheetBehavior.from(binding.containerRoot.parent as View).peekHeight =
                binding.containerRoot.height * 2 / 3
            bottomSheetBehavior.peekHeight = binding.containerRoot.height
            binding.containerRoot.parent.requestLayout()
        }

        this.setupRatio()
    }

    override fun setupViewModel() {
        viewModel.onSelectRatioCompletion = {
            adapter?.reloadData()
        }
    }

    // TODO Functions
    private fun setupRatio() {
        val context = context.guardLet { return }!!
        adapter = RatioAdapter(context = context, listenerCallback = object :
            OnItemClickListener<RatioModel> {
            override fun onItemClick(position: Int, model: RatioModel) {
                viewModel.setRatioSelected(position = position)
            }
        })
        val manager = LinearLayoutManager(context)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        binding.rcvRatio.layoutManager = manager
        binding.rcvRatio.adapter = adapter
        adapter?.setListItems(list = viewModel.getListRatio())
    }
}