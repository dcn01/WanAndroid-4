package com.tainzhi.android.wanandroid

import android.app.Application
import android.content.Context
import com.tainzhi.android.wanandroid.bean.SearchHistory
import com.tainzhi.android.wanandroid.db.HistoryDao
import com.tainzhi.android.wanandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

/**
 * @author:       tainzhi
 * @mail:         qfq61@qq.com
 * @date:         2020/3/1 下午4:43
 * @description:
 **/

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MockKoinRoomTest : KoinTest {

//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineScope = MainCoroutineScopeRule()

    @Mock
    private lateinit var mockContext: Context

    private lateinit var historyDao: HistoryDao

    @Before
    fun `setup`() {
        mockContext = Application()
        startKoin {
            androidContext(mockContext)
            modules(testAppModule)
        }
        historyDao = get()
    }

    @Test
    fun writeUserAndReadInList() {
        val searchHistory = SearchHistory(Date(), "search_key")

        // 不能使用runBloackingTest,会报错job not completed错误
        // 因为查询room只是suspend，
        // 可以替换使用GlobeScope.launch， 或者runBlockingTest{}
        // 这里使用的是test线程，而不是android amin thread， 不会报错： 不能再main thread 操作数据库
        GlobalScope.launch {
            historyDao.insertSearchKey(searchHistory)

            val queryResult = historyDao.getSearchHistory()
            assertThat(queryResult[0], equalTo(searchHistory))
        }
    }

    @After
    fun `tearDown`() {
        stopKoin()
    }
}