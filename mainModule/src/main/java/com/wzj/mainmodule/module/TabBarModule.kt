package com.wzj.mainmodule.module

import android.app.Application
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.mainmodule.R

open class TabBarModule : BaseRepository {
    constructor(application: Application) : super(application)

}