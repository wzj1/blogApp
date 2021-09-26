package com.ydph.lib_base.http.callback


/**
 *@time 2020/11/9
 *@author wangzhijie
 */
 open interface  HttpCallback<T>  {
     fun onSuccess(data:T?)
     fun failure(statusCode: Int,e: String?)
}