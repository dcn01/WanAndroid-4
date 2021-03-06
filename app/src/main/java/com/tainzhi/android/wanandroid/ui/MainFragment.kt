package com.tainzhi.android.wanandroid.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.tainzhi.android.common.base.ui.BaseFragment
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.wanandroid.ui.home.HomeFragment
import com.tainzhi.android.wanandroid.ui.navigation.NavigationFragment
import com.tainzhi.android.wanandroid.ui.system.SystemFragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment() {
    private val titleList = arrayOf("首页", "广场", "最新项目", "体系", "导航")
    private val fragmentList = arrayListOf<Fragment>()
    private val homeFragment by lazy { HomeFragment() }
    private val squareFragment by lazy { SquareFragment() }
    private val projectTypeFragment by lazy { ProjectTypeFragment.newInstance(0, true) } //最新项目
    private val systemFragment by lazy { SystemFragment() } // 体系
    private val navigationFragment by lazy { NavigationFragment() } // 导航
    private var onPageChangeCallback: ViewPager2.OnPageChangeCallback? = null

    init {
        fragmentList.run {
            add(homeFragment)
            add(squareFragment)
            add(projectTypeFragment)
            add(systemFragment)
            add(navigationFragment)
        }
    }

    override fun getLayoutResId() = R.layout.fragment_main

    override fun initView() {
        initViewPager()
    }

    override fun initData() {
    }

    private fun initViewPager() {
        viewPager.offscreenPageLimit = 1
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int) = fragmentList[position]

            override fun getItemCount() = titleList.size
        }


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = titleList[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        if (onPageChangeCallback == null) onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            }
        }
        onPageChangeCallback?.let { viewPager.registerOnPageChangeCallback(it) }
    }

    override fun onStop() {
        super.onStop()
        onPageChangeCallback?.let { viewPager.unregisterOnPageChangeCallback(it) }
    }

}