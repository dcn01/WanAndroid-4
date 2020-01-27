package com.tainzhi.android.wanandroid.repository

import com.tainzhi.android.wanandroid.api.WanClient
import com.tainzhi.android.wanandroid.base.BaseRepository
import com.tainzhi.android.wanandroid.base.Result
import com.tainzhi.android.wanandroid.bean.ArticleList

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/1/25 上午9:23
 * @description:
 **/

class CollectRepository : BaseRepository() {

    suspend fun getCollectArticles(page: Int): Result<ArticleList> {
        return safeApiCall(call = { requestCollectArticles(page) }, errorMessage = "网络错误")
    }

    suspend fun collectArticle(articleId: Int): Result<ArticleList> {
        return safeApiCall(call = { requestCollectArticle(articleId) }, errorMessage = "网络错误")
    }

    suspend fun unCollectArticle(articleId: Int): Result<ArticleList> {
        return safeApiCall(call = { requestCancelCollectArticle(articleId) }, errorMessage = "网络错误")
    }

    private suspend fun requestCollectArticles(page: Int): Result<ArticleList> =
            executeResponse(WanClient.service.getCollectArticles(page))

    private suspend fun requestCollectArticle(articleId: Int): Result<ArticleList> =
            executeResponse(WanClient.service.collectArticle(articleId))

    private suspend fun requestCancelCollectArticle(articleId: Int): Result<ArticleList> =
            executeResponse(WanClient.service.cancelCollectArticle(articleId))
}