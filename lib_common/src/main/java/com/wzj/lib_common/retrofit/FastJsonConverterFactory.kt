package com.wzj.lib_common.retrofit

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.nio.charset.Charset

open class FastJsonConverterFactory<T> : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<T, RequestBody?> {
        return Converter<T, RequestBody?> { value -> RequestBody.create(
            MEDIA_TYPE, JSON.toJSONString(
                value
            ).toByteArray(UTF_8)
        ) }
    }

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody?, T> {
        return Converter<ResponseBody?, T> {
                value -> when(value.string()){
                    is String -> value as T
                    else -> JSON.parseObject(value.string(), type)
                }
        }
    }

    companion object {
        private val MEDIA_TYPE: MediaType = "application/json;charset=UTF-8".toMediaType()
        private val UTF_8: Charset = Charset.forName("utf-8")
    }



}