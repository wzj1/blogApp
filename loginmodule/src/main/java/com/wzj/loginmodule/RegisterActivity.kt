package com.wzj.loginmodule

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.lib_common.base.act.BaseActivity
import com.wzj.lib_common.config.RouteConfig
import com.wzj.loginmodule.databinding.ActivityRegisterBinding
import com.wzj.loginmodule.module.RegisterModule

@Route(path = RouteConfig.ROUTE_REGISTER)
class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterModule>() {

    override fun initContentView(savedInstanceState: Bundle?): Int  = R.layout.activity_register
    override fun initVariableId(): Int  = BR.regModule
}