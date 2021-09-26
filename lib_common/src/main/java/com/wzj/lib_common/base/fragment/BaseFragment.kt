package com.wzj.lib_common.base.fragment

import androidx.databinding.ViewDataBinding
import me.goldze.mvvmhabit.base.BaseModel
import me.goldze.mvvmhabit.base.BaseViewModel
import me.goldze.mvvmhabit.base.BaseFragment

/**
 * fragment 的基类
 * @param T:ViewDataBinding
 * @param M:BaseViewModel<BaseModel>
 */
abstract class BaseFragment<T:ViewDataBinding,M:BaseViewModel<BaseModel>> : BaseFragment<T,M>() {

}