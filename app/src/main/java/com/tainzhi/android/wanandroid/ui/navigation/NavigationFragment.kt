package com.tainzhi.android.wanandroid.ui.navigation

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.wanandroid.adapter.NavigationAdapter
import com.tainzhi.android.wanandroid.adapter.VerticalTabAdapter
import com.tainzhi.android.wanandroid.base.ui.BaseVMFragment
import com.tainzhi.android.wanandroid.bean.Navigation
import com.tainzhi.android.wanandroid.util.dp2px
import com.tainzhi.android.wanandroid.view.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_navigation.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView

class NavigationFragment : BaseVMFragment<NavigationViewModel>() {

    private val navigationList = mutableListOf<Navigation>()
    private val tabAdapter by lazy { VerticalTabAdapter(navigationList.map { it.name }) }
    private val navigationAdapter by lazy { NavigationAdapter() }
    private val linearLayoutManager by lazy { LinearLayoutManager(activity) }
    override fun getLayoutResId() = R.layout.fragment_navigation

    override fun initVM(): NavigationViewModel = getViewModel()

    override fun initView() {
        navigationRecyclerView.run {
            layoutManager = linearLayoutManager
            addItemDecoration(SpaceItemDecoration(navigationRecyclerView.dp2px(10)))
            adapter = navigationAdapter
        }

        initTabLayout()
    }

    private fun initTabLayout() {
        tabLayout.addOnTabSelectedListener(object : VerticalTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabView?, position: Int) {
            }

            override fun onTabSelected(tab: TabView?, position: Int) {
                scrollToPosition(position)
            }
        })
    }

    private fun scrollToPosition(position: Int) {
        val firstPotion = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = linearLayoutManager.findLastVisibleItemPosition()
        when {
            position <= firstPotion || position >= lastPosition -> navigationRecyclerView.smoothScrollToPosition(position)
            else -> navigationRecyclerView.run {
                smoothScrollBy(0, this.getChildAt(position - firstPotion).top - this.dp2px(8))
            }
        }
    }

    override fun initData() {
        viewModel.getNavigation()
    }

    override fun startObserve() {
        viewModel.run {
            navigationList.observe(viewLifecycleOwner, Observer {
                it?.run { getNavigation(it) }
            })
        }
    }

    private fun getNavigation(navigationList: List<Navigation>) {
        this.navigationList.clear()
        this.navigationList.addAll(navigationList)
        tabLayout.setTabAdapter(tabAdapter)

        navigationAdapter.setNewData(navigationList)
    }
}