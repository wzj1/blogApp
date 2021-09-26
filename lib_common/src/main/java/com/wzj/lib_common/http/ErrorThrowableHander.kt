package com.wzj.lib_common.http

import org.apache.http.conn.ConnectTimeoutException
import java.io.InterruptedIOException
import java.net.*
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException

class ErrorThrowableHander {

    private var code:Int = -1
    private var msg:String = ""


    constructor(t:Throwable?){

        if (t!=null){
            when(t){
                //空指针异常
                is NullPointerException ->{
                    code =-1001
                    msg="空数据异常"
                }

                //运行时异常
                is IllegalAccessException->{
                    code =-1002
                    msg="APP异常，请退出重新启动"
                }

                //网络异常 ，连接超时 ，Socket 连接超时   Socket异常
                is UnknownHostException, is ConnectTimeoutException,is SocketTimeoutException ,is SocketException ->{
                    code =-1003
                    msg="网络异常，或服务器出小猜了，请稍后再试"
                }

                //连接服务器失败    无法连接远程地址与端口。
                is ConnectException ,is NoRouteToHostException  ->{
                    code =-1004
                    msg="连接服务器失败"
                }

                //SSL 失败    ssl 握手失败    协议异常
                is SSLException ,is SSLHandshakeException ,is SSLProtocolException ->{
                    code =-1005
                    msg="SSL证书或协议异常"
                }

                //操作IO 异常
                is InterruptedIOException ->{
                    code =-1006
                    msg="读取文件失败，请稍后再试"
                }

                else -> {
                    code =-1000
                    msg="未知错误"
                }

            }

        }

    }

    fun resultMsg():String{
        return this.msg
    }

    fun resultCode():Int{
        return this.code
    }




}