package com.apero.realistic.classes.dialog

import android.os.Bundle
import android.view.View
import com.apero.realistic.R
import com.apero.realistic.base.BaseDialogFragment
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.DialogLoadingBinding
import com.apero.realistic.utils.DelayUtil

class LoadingDialog(private val gotoPremium: (() -> Unit)? = null) :
    BaseDialogFragment<DialogLoadingBinding>() {
    override val layoutId: Int
        get() = R.layout.dialog_loading

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DelayUtil.runAfterDelay(time = 500) {
            this.startAnimation()
        }
    }

    override fun setupListener() {
        binding.imgBack.setOnClickListener {
            makeUIConfirmDismiss()
        }

        binding.lnSpeed.setOnClickListener {
            makeUIActiveSpeed()
        }
    }

    // TODO Functions
    private fun startAnimation() {
        binding.gifImageView.setImageResource(R.drawable.load_galaxy)
        binding.gifImageView2.setImageResource(R.drawable.loading_gif)
    }

    private fun delayHideNavigation() {
        DelayUtil.runAfterDelay(time = 100) {
            hideNavigationBar()
        }
    }

    // TODO Navigation
    private fun makeUIConfirmDismiss() {
        Router.makeUIDialogConfirm(childFragmentManager, onOkListener = {
            dismiss()
        }, onDismissCompletion = {
            delayHideNavigation()
        })
    }

    private fun makeUIActiveSpeed() {
        Router.makeUIActiveSpeed(childFragmentManager, onActiveListener = {
            gotoPremium?.invoke()
        }, onDismissCompletion = {
            delayHideNavigation()
        })
    }
}