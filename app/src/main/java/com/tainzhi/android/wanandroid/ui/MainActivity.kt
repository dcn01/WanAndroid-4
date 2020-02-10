package com.tainzhi.android.wanandroid.ui

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.tainzhi.android.wanandroid.R
import com.tainzhi.android.wanandroid.base.ui.BaseActivity
import com.tainzhi.android.wanandroid.util.gone
import com.tainzhi.android.wanandroid.util.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_drawer_nav_content_layout.*

class MainActivity : BaseActivity() {

    private lateinit var navController: NavController

    private val mainDestionIds = setOf(R.id.mainFragment, R.id.blogFragment, R.id.projectFragment)

    override fun getLayoutResId() = R.layout.activity_main

    override fun initView() {
        navController = findNavController(R.id.main_nav_host_fragment)

        val appBarConfig = AppBarConfiguration(mainDestionIds, main_drawer_layout)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfig)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(true)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in mainDestionIds) {
                main_bottom_nav.visible()
                toolbar.visible()
                main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                main_bottom_nav.gone()
                toolbar.gone()
                main_drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        val toggle = ActionBarDrawerToggle(
                this, main_drawer_layout, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close)
        main_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        main_bottom_nav.run {
            setupWithNavController(navController)
            setOnNavigationItemSelectedListener { menuItem ->
                NavigationUI.onNavDestinationSelected(menuItem, navController)
            }
        }

        initNavigationView()

    }

    private fun initNavigationView() {
        main_drawer_nav_view.setupWithNavController(navController)
        user_icon.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_loginFragment)
        }
        collections.setOnClickListener { navController.navigate(R.id.action_mainFragment_to_collectFragment) }
        browseHistory.setOnClickListener { navController.navigate(R.id
                .action_mainFragment_to_historyFragment)}
        otherInfo.setOnClickListener { navController.navigate(R.id
                .action_mainFragment_to_otherInfoFragment) }
    }

    override fun initData() {
    }

    override fun onBackPressed() {
        if (main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            main_drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_search_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.main_nav_host_fragment)
        return super.onOptionsItemSelected(item) || item.onNavDestinationSelected(navController)
    }
}
