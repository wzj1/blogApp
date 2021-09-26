package com.wzj.mymodule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.lib_common.base.fragment.BaseFragment
import com.wzj.lib_common.config.RouteConfig
import com.wzj.mymodule.databinding.MyFragmentBinding
import com.wzj.mymodule.module.MyModule


@Route(path = RouteConfig.ROUTE_MYINFO)
open class MyFragment : BaseFragment<MyFragmentBinding,MyModule>() {

    override fun initContentView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): Int=R.layout.my_fragment

    override fun initVariableId(): Int=BR.myModule

    override fun initData() {

        viewModel.setImage(binding.userHeadPortraitImg)

    }

}