package com.apero.realistic.classes.dialog

import com.apero.realistic.R
import com.apero.realistic.base.BaseDialogFragment
import com.apero.realistic.databinding.DialogOutOfCreditsBinding

class OutOfCreditsDialog (private val onClickListener: () -> Unit): BaseDialogFragment<DialogOutOfCreditsBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_out_of_credits

    override fun setupListener() {
        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.lnGoPremium.setOnClickListener{
            onClickListener()
        }
    }
}
