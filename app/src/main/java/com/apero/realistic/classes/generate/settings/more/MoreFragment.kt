package com.apero.realistic.classes.generate.settings.more

import com.apero.realistic.R
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.databinding.FragmentMoreBinding

class MoreFragment: BaseFragment<FragmentMoreBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_more

    override fun setupUI() {
        binding.seekBarAlpha.setCurrentValues(leftValue = 0, rightValue = 0)
        binding.seekBarStrength.setCurrentValues(leftValue = 0, rightValue = 0)
        binding.seekBarScale.setCurrentValues(leftValue = 0, rightValue = 10)
        binding.seekBarStep.setCurrentValues(leftValue = 0, rightValue = 30)
    }

    override fun setupListener() {
    }

    override fun setupViewModel() {
    }
}