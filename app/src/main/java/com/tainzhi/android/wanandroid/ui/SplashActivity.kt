package com.tainzhi.android.wanandroid.ui

import android.text.Html
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.wanandroid.adapter.SplashAdapter
import com.tainzhi.android.wanandroid.base.ui.BaseActivity
import com.tainzhi.android.wanandroid.binding.fromN
import com.tainzhi.android.wanandroid.util.startKtxActivity
import com.tainzhi.android.wanandroid.view.ScrollLinearLayoutManager
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class SplashActivity : BaseActivity() {

    private val imageSrcId = arrayListOf(
            R.drawable.android_0,
            R.drawable.android_1,
            R.drawable.android_2,
            R.drawable.android_3,
            R.drawable.android_4,
            R.drawable.android_5)
    private val splashAdapter by lazy { SplashAdapter(imageSrcId) }

    override fun getLayoutResId() = R.layout.activity_splash

    override fun initView() {
        splashRecyclerView.run {
            adapter = splashAdapter
            layoutManager = ScrollLinearLayoutManager(this@SplashActivity)
            smoothScrollToPosition(Integer.MAX_VALUE / 2)
        }
        splashLogoTv.text = if (fromN()) Html.fromHtml(getString(R.string.app_logo_html), Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(getString(R.string.app_logo_html))

    }

    override fun initData() {
        launch {
            delay(3000L)
            startKtxActivity<MainActivity>()
            finish()
        }
    }
}