package com.wzj.msgmodule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.alibaba.android.arouter.facade.annotation.Route
import com.wzj.lib_common.base.fragment.BaseFragment
import com.wzj.lib_common.config.RouteConfig
import com.wzj.msgmodule.databinding.MsgFragmentBinding
import com.wzj.msgmodule.module.MsgModule


@Route(path = RouteConfig.ROUTE_MESSAGE)
open class MsgFragment : BaseFragment<MsgFragmentBinding,MsgModule>() {

    override fun initContentView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): Int =R.layout.msg_fragment

    override fun initVariableId(): Int =BR.msgModule

}