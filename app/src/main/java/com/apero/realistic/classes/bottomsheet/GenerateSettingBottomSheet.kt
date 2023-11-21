package com.apero.realistic.classes.bottomsheet

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.apero.realistic.R
import com.apero.realistic.base.BaseBottomSheetDialogFragment
import com.apero.realistic.base.BaseFragment
import com.apero.realistic.base.adapter.BasePagerAdapter
import com.apero.realistic.base.ext.guardLet
import com.apero.realistic.base.ext.setColorFilter
import com.apero.realistic.classes.generate.settings.keyword.TagKeywordFragment
import com.apero.realistic.classes.generate.settings.more.MoreFragment
import com.apero.realistic.classes.generate.settings.style.StyleFragment
import com.apero.realistic.databinding.BottomSheetGenerateSettingBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

enum class GenerateSettingKey {
    KEY_WORD,
    STYLE,
    MORE;

    fun getTitle(): String {
        return when(this) {
            KEY_WORD -> return "Tag Keywords"
            STYLE -> return "Styles"
            MORE -> return "More"
        }
    }

    fun getUI(): BaseFragment<*> {
        return when(this) {
            KEY_WORD -> return TagKeywordFragment()
            STYLE -> return StyleFragment()
            MORE -> return MoreFragment()
        }
    }
}

class GenerateSettingUI(val key: GenerateSettingKey, var selected: Boolean)

class GenerateSettingBottomSheet :
    BaseBottomSheetDialogFragment<BottomSheetGenerateSettingBinding>() {
    private var currentPosition = 0
    private var tagKeywordFragment: TagKeywordFragment? = null
    private var styleFragment: StyleFragment? = null
    private var moreFragment: MoreFragment? = null
    private val mListFragment: ArrayList<Fragment> = ArrayList()
    var settings: ArrayList<GenerateSettingUI> = arrayListOf()

    override val layoutId: Int
        get() = R.layout.bottom_sheet_generate_setting

    override fun setupUI() {
        super.setupUI()
        dialog?.setOnShowListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
            bottomSheetBehavior.isHideable = false
            BottomSheetBehavior.from(binding.containerRoot.parent as View).peekHeight =
                binding.containerRoot.height / 2
            bottomSheetBehavior.peekHeight = binding.containerRoot.height
            binding.containerRoot.parent.requestLayout()
        }

        this.setupViewPager()
    }

    override fun setupListener() {
        super.setupListener()

        binding.imgNext.setOnClickListener {
            gotoScreen(position = currentPosition + 1)
        }

        binding.imgBack.setOnClickListener {
            gotoScreen(position = currentPosition - 1)
        }
    }

    // TODO Functions
    private fun setupViewPager() {
        settings.forEach {
            mListFragment.add(it.key.getUI())

            when (it.key) {
                GenerateSettingKey.KEY_WORD -> tagKeywordFragment = it.key.getUI() as TagKeywordFragment
                GenerateSettingKey.STYLE -> styleFragment = it.key.getUI() as StyleFragment
                GenerateSettingKey.MORE -> moreFragment = it.key.getUI() as MoreFragment
            }
        }
        currentPosition = settings.indexOfFirst { it.selected }
        val pagerAdapter = BasePagerAdapter(
            fragmentManager = childFragmentManager,
            listFragment = mListFragment,
            title = arrayOf("", "", "")
        )
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = mListFragment.count()
        binding.viewPager.setCurrentItem(currentPosition, false)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                this@GenerateSettingBottomSheet.currentPosition = position
                updateUINextBack(isCanNext = position < mListFragment.count() - 1, isCanBack = position > 0)
                updateTitle()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        updateUINextBack(isCanNext = currentPosition < mListFragment.count() - 1, isCanBack = currentPosition > 0)
        updateTitle()
    }

    private fun updateUINextBack(isCanNext: Boolean, isCanBack: Boolean) {
        val context = context.guardLet { return }!!
        binding.imgNext.setColorFilter(
            context,
            colorId = if (isCanNext) R.color.color_FFFFFF else R.color.color_777777
        )
        binding.imgNext.isEnabled = isCanNext
        binding.imgBack.setColorFilter(
            context,
            colorId = if (isCanBack) R.color.color_FFFFFF else R.color.color_777777
        )
        binding.imgBack.isEnabled = isCanBack
    }

    private fun updateTitle() {
        binding.tvTitle.text = settings[currentPosition].key.getTitle()
    }

    private fun gotoScreen(position: Int) {
        if (position < 0 || position > mListFragment.count() - 1) return

        currentPosition = position
        binding.viewPager.setCurrentItem(position, true)
    }
}