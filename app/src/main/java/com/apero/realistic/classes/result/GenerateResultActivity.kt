package com.apero.realistic.classes.result

import androidx.activity.viewModels
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.ActivityGenerateResultBinding

class GenerateResultActivity: BaseActivity<ActivityGenerateResultBinding>() {
    private val viewModel: GenerateResultViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_generate_result

    override fun setupUI() {
    }

    override fun setupListener() {
        binding.imgClose.setOnClickListener {
            finish()
        }

        binding.imgEdit.setOnClickListener {
            makeUIEditPrompt()
        }
    }

    override fun setupViewModel() {
    }

    // TODO Navigation
    private fun makeUIEditPrompt() {
        Router.makeUIBottomSheetEditPrompt(from = this)
    }
}