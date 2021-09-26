package com.wzj.lib_common.http.header

import com.wzj.lib_common.sp.SPUtil
import okhttp3.Headers

/**
 * 统一请求头处理
 */
object BaseHeader {
    //存储请求头信息
    private var headers:Headers = Headers.headersOf()


    /**
     *   添加 请求头
     * @param keyValue Array<out KeyValue?>
     */
    fun setHeader(vararg keyValue: KeyValue?){
        if (keyValue.size>0){
          val builder =  Headers.headersOf().newBuilder()
            for (i in keyValue){
                if (i==null) break
                if (i.key==null||i.key=="") break
                builder.add(i.key,i.value.toString())

            }

            if (builder.build().size>0) {
                headers =  headers.newBuilder().addAll(builder.build()).build()
                init()
            }
        }
    }

    /**
     *   添加 请求头
     * @param keyValue Array<out KeyValue?>
     */
    fun setHeader( key:String,value: String?){
        val builder =  Headers.headersOf().newBuilder()
        builder.add(key,value.toString())
       headers = headers.newBuilder().add(key,value.toString()).build()
    }

    /**
     * 获取请求头信息
     * @return MutableMap<String,Any?>
     */
    fun getHeaders():Headers{

        return init()
    }


    private  fun init(): Headers {
      val builder =   headers.newBuilder()
        if (headers == null) headers = Headers.headersOf()
        builder.add("token", SPUtil.instance.getValue<String>(SPUtil.SP_TOKEN,""))
        if (headers["Content-Type"] == null || headers["Content-Type"]!! != "application/json;charset=UTF-8"&&headers["Content-Type"]!! != "application/json;charset=utf-8" && headers["Content-Type"]!! != "application/json;"){
            builder.add("Content-Type", "application/json;charset=UTF-8")
        }
        builder.add("timestamp",System.currentTimeMillis().toString())
        builder.add("sign",System.currentTimeMillis().toString())
        return builder.build()
    }

}