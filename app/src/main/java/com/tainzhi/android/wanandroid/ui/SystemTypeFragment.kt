package com.tainzhi.android.wanandroid.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.wanandroid.adapter.HomeArticleAdapter
import com.tainzhi.android.wanandroid.base.ui.BaseVMFragment
import com.tainzhi.android.wanandroid.util.Preference
import com.tainzhi.android.wanandroid.util.dp2px
import com.tainzhi.android.wanandroid.util.toast
import com.tainzhi.android.wanandroid.view.CustomLoadMoreView
import com.tainzhi.android.wanandroid.view.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_system_type.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SystemTypeFragment : BaseVMFragment<ArticleViewModel>() {

    private val isLogin by Preference(Preference.IS_LOGIN, false)

    private val cid by lazy { arguments?.getInt(CID) }
    private val isBlog by lazy { arguments?.getBoolean(BLOG) ?: false } // 区分是体系下的文章列表还是公众号下的文章列表
    private val systemTypeAdapter by lazy { HomeArticleAdapter() }

    companion object {
        private const val CID = "cid"
        private const val BLOG = "blog"
        fun newInstance(cid: Int, isBlog: Boolean): SystemTypeFragment {
            val fragment = SystemTypeFragment()
            val bundle = Bundle()
            bundle.putBoolean(BLOG, isBlog)
            bundle.putInt(CID, cid)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutResId() = R.layout.fragment_system_type

    override fun initView() {
        initRecycleView()
    }

    override fun initVM(): ArticleViewModel = getViewModel()

    override fun initData() {
        refresh()
    }

    private fun initRecycleView() {
        systemTypeRefreshLayout.setOnRefreshListener { refresh() }
        systemTypeAdapter.run {
            setOnItemClickListener { _, _, position ->
                Navigation.findNavController(systemTypeRecycleView).navigate(R.id
                        .action_tabFragment_to_browserActivity, bundleOf
                (BrowserActivity.URL to systemTypeAdapter.data[position].link))
            }
            onItemChildClickListener = this@SystemTypeFragment.onItemChildClickListener

            setLoadMoreView(CustomLoadMoreView())
            setOnLoadMoreListener({ loadMore() }, systemTypeRecycleView)
        }
        systemTypeRecycleView.run {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SpaceItemDecoration(systemTypeRecycleView.dp2px(10)))
            adapter = systemTypeAdapter
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.articleStar -> {
                if (isLogin) {
                    systemTypeAdapter.run {
                        data[position].run {
                            collect = !collect
                            mViewModel.collectArticle(id, collect)
                        }
                        notifyDataSetChanged()
                    }
                } else {
                    Navigation.findNavController(systemTypeRecycleView).navigate(R.id
                            .action_tab_to_login)
                }
            }
        }
    }

    private fun loadMore() {
        loadData(false)
    }

    private fun refresh() {
        systemTypeAdapter.setEnableLoadMore(false)
        loadData(true)
    }


    private fun loadData(isRefresh: Boolean) {
        cid?.let {
            if (this.isBlog)
                mViewModel.getBlogArticleList(isRefresh, it)
            else
                mViewModel.getSystemTypeArticleList(isRefresh, it)
        }
    }

    override fun startObserve() {
        mViewModel.uiState.observe(this, Observer {
            systemTypeRefreshLayout.isRefreshing = it.showLoading

            it.showSuccess?.let { list ->
                systemTypeAdapter.run {
                    if (it.isRefresh) replaceData(list.datas)
                    else addData(list.datas)
                    setEnableLoadMore(true)
                    loadMoreComplete()
                }
            }

            if (it.showEnd) systemTypeAdapter.loadMoreEnd()

            it.showError?.let { message ->
                activity?.toast(if (message.isBlank()) "网络异常" else message)
            }
        })
    }
}