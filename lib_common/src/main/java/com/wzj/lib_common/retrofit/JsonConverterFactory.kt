package com.wzj.lib_common.retrofit

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.ydph.lib_common.log.Logs
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 *
 * @param T
 * @constructor
 */
open class JsonConverterFactory<T>(val isEncryption: Boolean) : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<T, RequestBody?> {
        return Converter<T, RequestBody?> { value ->

            when (value) {
                is JSONObject -> {
                    RequestBody.create(
                        MEDIA_TYPE, JSON.toJSONString(
                            value
                        ).toByteArray(Charsets.UTF_8)
                    )
                }
                is JSONArray -> {
                    RequestBody.create(
                        MEDIA_TYPE, JSON.toJSONString(
                            value
                        ).toByteArray(UTF_8)
                    )
                }

                is String -> {
                    RequestBody.create(
                        MEDIA_TYPE, value
                    )
                }
                else -> RequestBody.create(
                    MEDIA_TYPE, JSON.toJSONString(
                        value
                    ).toByteArray(UTF_8)
                )
            }
        }
//        }


    }

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody?, T> {
        return Converter<ResponseBody?, T> { value ->
            var data =  value.string()
//            Logs.d("请求解密后的JSON", JSONObject.toJSONString(data))
            JSONObject.parseObject(data,type)
        }

    }

    companion object {
        private val MEDIA_TYPE: MediaType = "application/json;charset=UTF-8".toMediaType()
        private val UTF_8: Charset = Charset.forName("utf-8")
    }


}