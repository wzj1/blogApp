package com.wzj.myblogapp

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.lib_common.base.act.BaseActivity
import com.wzj.lib_common.config.RouteConfig
import com.wzj.myblogapp.appModlue.AppModlue
import com.wzj.myblogapp.databinding.ActivityMainBinding

@Route(path = RouteConfig.ROUTE_APP)
open class MainActivity : BaseActivity<ActivityMainBinding, AppModlue>() {

    override fun initContentView(savedInstanceState: Bundle?): Int =R.layout.activity_main

    override fun initVariableId(): Int  = BR.appModule

    override fun initData() {

        viewModel.getPublicIp()
    }
}