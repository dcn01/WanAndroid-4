package com.tainzhi.android.wanandroid.ui

import android.content.Context
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.kennyc.view.MultiStateView
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.wanandroid.adapter.HomeArticleAdapter
import com.tainzhi.android.common.base.ui.BaseVMFragment
import com.tainzhi.android.common.util.toast
import com.tainzhi.android.wanandroid.databinding.FragmentCollectBinding
import com.tainzhi.android.wanandroid.view.CustomLoadMoreView
import com.tainzhi.android.wanandroid.view.SpaceItemDecoration
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_collect.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class CollectFragment : BaseVMFragment<ArticleViewModel>(useBinding = true) {
    private val articleAdapter by lazy { HomeArticleAdapter() }
    override fun getLayoutResId() = R.layout.fragment_collect

    override fun initVM(): ArticleViewModel = getViewModel()

    override fun initView() {
        toolbar.setTitle(R.string.my_collection)
        toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback { onBack() }

        collectRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity as Context, R.color.color_secondary))

        (mBinding as FragmentCollectBinding).viewModel = mViewModel

        collectRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SpaceItemDecoration(context.resources.getDimension(R.dimen.margin_small)))
        }

        initAdapter()
    }

    override fun initData() {
        refresh()
    }

    private fun initAdapter() {
        articleAdapter.run {
            setOnItemClickListener { _, _, position ->
                mViewModel.insertBrowseHistory(articleAdapter.data[position])

                val action = BrowserFragmentDirections.actionGlobalBrowserFragment(articleAdapter
                        .data[position].link)
                findNavController().navigate(action)

            }
            setOnItemChildClickListener(this@CollectFragment.itemChildClickListener)
            loadMoreModule.run {
                loadMoreView = CustomLoadMoreView()
                setOnLoadMoreListener { loadMore() }
            }
        }
        collectRecyclerView.adapter = articleAdapter
    }

    private val itemChildClickListener = OnItemChildClickListener { _, view, position ->
        when (view.id) {
            R.id.collectIv -> {
                articleAdapter.run {
                    data[position].run {
                        collect = !collect
                        mViewModel.collectArticle(originId, collect)
                    }
                    remove(position)
                }
            }
        }
    }

    private fun refresh() {
        articleAdapter.loadMoreModule.isEnableLoadMore = false
        mViewModel.getCollectArticleList(true)
    }

    private fun loadMore() {
        mViewModel.getCollectArticleList(false)
    }

    override fun startObserve() {

        mViewModel.apply {

            uiState.observe(viewLifecycleOwner, Observer {

                it.showSuccess?.let { list ->
                    collectMultiStateView.viewState = MultiStateView.ViewState.CONTENT
                    list.datas.forEach { it.collect = true }
                    articleAdapter.run {
                        if (it.isRefresh) setList(list.datas)
                        else addData(list.datas)
                        loadMoreModule.run {
                            isEnableLoadMore = true
                            loadMoreComplete()
                        }
                    }
                }
                if (!it.showLoading) {
                    if (articleAdapter.data.isEmpty()) {
                        collectMultiStateView.viewState = MultiStateView.ViewState.EMPTY
                    }
                }

                if (it.showEnd) articleAdapter.loadMoreModule.loadMoreEnd()

                it.showError?.let { message ->
                    activity?.toast(if (message.isBlank()) "Net error" else message)
                    collectMultiStateView.viewState = MultiStateView.ViewState.ERROR
                }
            })
        }
    }

    private fun onBack() {
        findNavController().navigateUp()
    }

}