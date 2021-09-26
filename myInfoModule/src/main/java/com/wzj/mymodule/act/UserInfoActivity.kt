package com.wzj.mymodule.act

import android.os.Bundle
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.lib_common.base.act.BaseActivity
import com.wzj.lib_common.config.RouteConfig
import com.wzj.mymodule.BR
import com.wzj.mymodule.R
import com.wzj.mymodule.databinding.ActivityUserInfoBinding
import com.wzj.mymodule.module.UserInfoModule

@Route(path = RouteConfig.ROUTE_MY_USERINFO)
class UserInfoActivity : BaseActivity<ActivityUserInfoBinding, UserInfoModule>() {
    override fun initContentView(savedInstanceState: Bundle?): Int  = R.layout.activity_user_info

    override fun initVariableId(): Int  = BR.userinfo


    override fun initData() {

        binding.idUserConstellTv.setOnClickListener(viewModel.showConstellAtion(binding.idUserConstellTv))

        binding.idUserBirthTv.setOnClickListener {
            viewModel.timePicker(binding.idUserBirthTv)
        }
    }

}