package com.apero.realistic.classes.main

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.apero.realistic.R
import com.apero.realistic.base.BaseActivity
import com.apero.realistic.base.adapter.BasePagerAdapter
import com.apero.realistic.base.ext.disposedBy
import com.apero.realistic.base.ext.gone
import com.apero.realistic.classes.discovery.DiscoveryFragment
import com.apero.realistic.classes.generate.GenerateFragment
import com.apero.realistic.classes.router.Router
import com.apero.realistic.databinding.ActivityMainBinding
import com.apero.realistic.layout.BaseBottomTabLayout

class MainActivity: BaseActivity<ActivityMainBinding>() {
    private val generateFragment: GenerateFragment = GenerateFragment()
    private val discoveryFragment: DiscoveryFragment = DiscoveryFragment()
    private val mListFragment: ArrayList<Fragment> = ArrayList()
    private val viewModel: MainViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun setupUI() {
        this.setupViewPager()
    }

    override fun setupListener() {
        binding.tabLayout.setOnTabSelectedListener(listener = object: BaseBottomTabLayout.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {

            }

            override fun onTabSelect(position: Int) {
                binding.viewPager.setCurrentItem(position, false)
            }
        })

        binding.imgSetting.setOnClickListener {
            makeUISetting()
        }

        binding.imgProfile.setOnClickListener {
            makeUIProfile()
        }

        binding.ctlLoading.setOnClickListener {  }
    }

    override fun setupViewModel() {
        viewModel.makeProgress
            .subscribe {
                binding.ctlLoading.gone()
            }
            .disposedBy(bag)

        viewModel.startFetchAllData()
    }

    // TODO Functions
    private fun setupViewPager() {
        mListFragment.add(discoveryFragment)
        mListFragment.add(generateFragment)
        val pagerAdapter = BasePagerAdapter(fragmentManager = supportFragmentManager, listFragment = mListFragment, title = arrayOf("", ""))
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.offscreenPageLimit = 2
        binding.viewPager.setCurrentItem(0, false)
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.tabLayout.activeTab(tabIndex = position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        binding.tabLayout.activeTab(tabIndex = 0)
    }

    // TODO Navigation
    private fun makeUISetting() {
        Router.makeUISetting(this)
    }

    private fun makeUIProfile() {
        Router.makeUIProfile(this)
    }
}
