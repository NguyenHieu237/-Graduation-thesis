package com.apero.realistic.classes.dialog

import com.apero.realistic.R
import com.apero.realistic.base.BaseDialogFragment
import com.apero.realistic.databinding.DialogConfirmBinding

class ConfirmDialog(private val onOkListener: (() -> Unit)? = null) : BaseDialogFragment<DialogConfirmBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_confirm

    override fun setupUI() {

    }

    override fun setupListener() {
        binding.btnOK.setOnClickListener {
            onOkListener?.invoke()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }
}
