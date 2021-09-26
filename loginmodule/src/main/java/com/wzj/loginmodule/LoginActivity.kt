package com.wzj.loginmodule

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.lib_common.base.act.BaseActivity
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.sp.SPUtil
import com.wzj.loginmodule.databinding.ActivityLoginBinding
import com.wzj.loginmodule.module.LoginModule

@Route(path = RouteConfig.ROUTE_LOGIN)
open class LoginActivity : BaseActivity<ActivityLoginBinding,LoginModule>() {


    override fun initContentView(savedInstanceState: Bundle?): Int = R.layout.activity_login

    override fun initVariableId(): Int  = BR.loginViewModule

//    override fun initViewModel(): LoginModule {
//        return ViewModelProviders.of(this,ViewModelProvider.AndroidViewModelFactory(application).create(LoginModule::class.java))
//    }

    override fun initData() {
        val userInfo = SPUtil.instance.getUserInfo()
        if (!userInfo.userName.isNullOrBlank() && !userInfo.userPwd.isNullOrBlank()){
            viewModel.userName.set(userInfo.userName)
            viewModel.password.set(userInfo.userPwd)
            viewModel.toHome()
        }

    }
}