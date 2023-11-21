package com.apero.realistic.classes.dialog

import com.apero.realistic.R
import com.apero.realistic.base.BaseDialogFragment
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.databinding.DialogTryArtworkBinding

class ArtworkPreviewDialog: BaseDialogFragment<DialogTryArtworkBinding>() {
    var onTryThisCompletion: (() -> Unit)? = null

    override val layoutId: Int
        get() = R.layout.dialog_try_artwork

    override fun setupUI() {
        val context = context.guardLet { return }!!
        binding.viewDownload.activeBlurView(context = context, viewGroup = binding.ctlContainer)
        binding.viewLike.setIcon(context = context, iconId = R.drawable.ic_heart)
        binding.viewLike.activeBlurView(context = context, viewGroup = binding.ctlContainer)
    }

    override fun setupListener() {
        binding.imgClose.setOnClickListener {
            dismiss()
        }

        binding.lnTryThis.setOnClickListener {
            dismiss()
            onTryThisCompletion?.invoke()
        }
    }
}