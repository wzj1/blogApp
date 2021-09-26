package com.wzj.homemodule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.homemodule.databinding.HomeFragmentBinding
import com.wzj.homemodule.module.HomeModule
import com.wzj.lib_common.base.fragment.BaseFragment
import com.wzj.lib_common.config.RouteConfig


@Route(path = RouteConfig.ROUTE_HOME)
open class HomeFragment : BaseFragment<HomeFragmentBinding,HomeModule>() {

    override fun initContentView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): Int =R.layout.home_fragment

    override fun initVariableId(): Int =BR.homeModule

}