package com.wzj.lib_common.http.interceptor

import android.os.Build
import android.os.NetworkOnMainThreadException
import androidx.annotation.RequiresApi
import com.alibaba.fastjson.util.IOUtils.UTF8
import com.wzj.lib_common.RSAUtil
import com.wzj.lib_common.http.header.BaseHeader
import com.wzj.lib_common.retrofit.ToRequestBody
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.lib_common.util.NetworkUtils
import com.ydph.lib_common.log.Logs
import com.ydph.lib_common.log.Logs.i
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


/**
 *@time 2020/11/9
 *@author wangzhijie
 */
open class LoggingInterceptor(val isEncryption: Boolean) : Interceptor {

    private val TAG = LoggingInterceptor::class.java.simpleName

    private val pathLogin = "https://pv.sohu.com/"
    private val baidu = "http://www.baidu.com"
    private val baidus = "https://www.baidu.com"


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(IOException::class)
    private fun encrypt(request: Request): Request {
        var request = request
        val requestBody = request.body
        if (isEncryption) {
            if (requestBody != null) {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                var charset = Charset.forName("UTF-8")
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(charset)
                }
                val string = buffer.readString(charset!!)
                i(TAG, "加密前的数据为:$string")
                var encryptStr = RSAUtil.encryptByPublic(string)
                val body = ToRequestBody.getRequestBodyAny(encryptStr)
                request = request
                    .newBuilder()
                    .headers(
                        if (BaseHeader.getHeaders().size > 0)
                            BaseHeader.getHeaders()
                        else Headers.headersOf()
                    )
                    .post(body)
                    .build()
                val buffer1 = Buffer()
                request.body!!.writeTo(buffer1)
                var charset1 = UTF8
                val contentType1 = request.body!!.contentType()
                if (contentType1 != null) {
                    charset1 = contentType1.charset(charset1)
                }
                val string1 = buffer1.readString(charset1)
                i(TAG, "请求的xin数据为:$string1")
            }
        }
        return request
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (NetworkUtils.isConnected(ApplacationContextHolder.mContext)) {
           val url =  chain.request().url.toString()
            if (!url.startsWith(pathLogin)&&!url.startsWith(baidu)&&!url.startsWith(baidus)) {
                if (isEncryption) {
                    var request: Request = chain.request()

                    val bobyBuilder = FormBody.Builder(Charset.forName("UTF-8"))
                    Logs.d("请求的URL：---->", url.toString())
                    if (request.method == "POST") {
                        when (val body = request.body) {
                            is FormBody -> {
                                for (j in 0 until body.size) {
                                    if (j != body.size - 1) {
                                        bobyBuilder.addEncoded(
                                            body.encodedName(j),
                                            body.encodedValue(j)
                                        )
                                    }
                                }
                            }
                        }
                        bobyBuilder.build()
                    }

                    var oldRequest = chain.request()

                    oldRequest = encrypt(oldRequest)

                    // 新的请求
                    val requestBuilder = oldRequest.newBuilder()
                    requestBuilder.method(oldRequest.method, oldRequest.body)

                    val newRequest = requestBuilder.build()
                    var response: Response? = null

//            try {
                    response = chain.proceed(newRequest);
                    val responseBody = response.body

//               if (HttpHeaders.hasBody(response)) {
                    val source = responseBody?.source()
                    source?.request(Long.MAX_VALUE); // Buffer the entire body.
                    val buffer = source?.buffer

                    var charset = UTF8;
                    val contentType = responseBody?.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(UTF8)
                    }
                    val body:String? = buffer?.clone()?.readString(charset)

                    return try {
                        i(TAG, "解密前的数据为:$body")
                        val decryptStr = RSAUtil.decrypt(body)

                        i(TAG, "解密后的数据为:$decryptStr")
                        response.newBuilder().body(ToRequestBody.getResponseBody(decryptStr))
                           .build()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        response
                    }
                } else {
                  val response =   chain.proceed(reess(chain))

                    val responseBody = response.body

                    val source = responseBody?.source()
                    source?.request(Long.MAX_VALUE); // Buffer the entire body.
                    val buffer = source?.buffer

                    var charset = UTF8;
                    val contentType = responseBody?.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(UTF8)
                    }
                    val body:String? = buffer?.clone()?.readString(charset)

                    return try {
                        i(TAG, "解密前的数据为:$body")
                        val decryptStr = RSAUtil.decrypt(body)

                        i(TAG, "解密后的数据为:$decryptStr")
                        response.newBuilder().body(ToRequestBody.getResponseBody(decryptStr))
                            .build()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        response
                    }
                }
            }
        } else {
            throw NetworkOnMainThreadException()
        }
        return chain.proceed(chain.request())
    }


    /**
     *  请求第三方地址时，不使用 解密加密处理
     * @param chain Chain
     * @return Request
     */
   private fun reess(chain: Interceptor.Chain): Request {
        var request = chain.request()
        return request
            .newBuilder()
            .headers(
                if (BaseHeader.getHeaders().size > 0)
                    BaseHeader.getHeaders()
                else Headers.headersOf()
            )
            .post(request.body!!)
            .build()
    }
}