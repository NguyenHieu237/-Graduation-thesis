package com.apero.realistic.classes.dialog

import com.apero.realistic.R
import com.apero.realistic.base.BaseDialogFragment
import com.apero.realistic.databinding.DialogSpeedToFutureBinding

class SpeedToFutureDialog(private val onActiveListener: (() -> Unit)? = null) :
    BaseDialogFragment<DialogSpeedToFutureBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_speed_to_future

    override fun setupUI() {

    }

    override fun setupListener() {
        binding.linerGoPremium.setOnClickListener {
            onActiveListener?.invoke()
        }

        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }
}
