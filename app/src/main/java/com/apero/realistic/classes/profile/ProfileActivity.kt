package com.apero.realistic.classes.profile

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.base.adapter.BasePagerAdapter
import com.apero.realistic.classes.profile.favorites.FavoriteFragment
import com.apero.realistic.classes.profile.projects.ProjectFragment
import com.apero.realistic.databinding.ActivityProfileBinding
import com.apero.realistic.widget.myswitch.MySwitch

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    private val myProjectsFragment: ProjectFragment = ProjectFragment()
    private val favoriteFragment: FavoriteFragment = FavoriteFragment()
    private val mListFragment: ArrayList<Fragment> = ArrayList()

    override val layoutId: Int
        get() = R.layout.activity_profile

    override fun setupUI() {
        setupViewPager()
    }

    override fun setupListener() {
        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.mySwitch.setOnChangeAttemptListener(listener = object :
            MySwitch.OnChangeAttemptListener {
            override fun onChangeAttempted(isChecked: Boolean) {
                binding.viewPager.setCurrentItem(if (isChecked) 0 else 1, true)
            }
        })
    }

    override fun setupViewModel() {
    }

    // TODO Functions
    private fun setupViewPager() {
        mListFragment.add(myProjectsFragment)
        mListFragment.add(favoriteFragment)
        val pagerAdapter = BasePagerAdapter(
            fragmentManager = supportFragmentManager,
            listFragment = mListFragment,
            title = arrayOf("", "")
        )
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.setCurrentItem(0, false)
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}