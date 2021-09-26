package com.wzj.lib_common.retrofit

import com.google.gson.Gson
import com.ydph.lib_common.log.Logs
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.File
import java.util.regex.Pattern

object ToRequestBody {
     var dataMap: HashMap<String, Any?> = hashMapOf()

    /**
     * 封装数据 并返回请求体
     */
    open fun <T> getRequestBody(map: T): RequestBody {
        dataMap = map as HashMap<String, Any?>
        Logs.d("发起请求数据", "\n\r${Gson().toJson(dataMap)}")
        val map = HashMap<String?,Any?>()
        map["data"] = Gson().toJson(dataMap)
        return Gson().toJson(dataMap).toRequestBody("application/json; charset=utf-8".toMediaType())
    }


    fun getRequestBodyAny(any: Any):RequestBody = Gson().toJson(any).toRequestBody("application/json; charset=utf-8".toMediaType())
    fun getRequestBodyAny(jsonStr: String):RequestBody =jsonStr.toRequestBody("application/json; charset=utf-8".toMediaType())

    fun getResponseBodyAny(any: Any): ResponseBody =  Gson().toJson(any).toResponseBody("application/json; charset=utf-8".toMediaType())

    /**
     * 封装数据 并返回请求体
     */
    fun getRequestBody(): RequestBody {
        return  Gson().toJson(dataMap).toRequestBody("application/json; charset=utf-8".toMediaType())
    }




    open fun convertMapToMediaBody(map: Map<*, *>?): RequestBody? {
        return RequestBody.create(
            "multipart/form-data; charset=utf-8".toMediaTypeOrNull(),
            org.json.JSONObject(map).toString()
        )
    }

    fun getRequestBodyFrom(map: HashMap<String, Any?>, file: File): List<MultipartBody.Part> {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        for (i in map.entries) {
            builder.addFormDataPart(i.key, i.value.toString())
        }

        return builder.addFormDataPart(
            "file",
            file.name,
            file.asRequestBody("multipart/form-data".toMediaType())
        ).build().parts
    }
    fun getRequestBodyFrom(file: File,requestBody: RequestBody): List<MultipartBody.Part> {

      val builder1 =  MultipartBody.Part.create(requestBody)

       val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        builder.addPart(builder1)
        return builder.addFormDataPart(
            "file",
            file.name,
            file.asRequestBody("multipart/form-data".toMediaType())
        ).build().parts
    }

    fun getRequestBodyFrom(file: File): List<MultipartBody.Part> {


       val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        return builder.addFormDataPart(
            "file",
            file.name,
            file.asRequestBody("multipart/form-data".toMediaType())
        ).build().parts
    }



    fun getRequestBody(map: HashMap<String, Any?>, file: File): RequestBody {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        for (i in map.entries) {
            builder.addFormDataPart(i.key, i.value.toString())
        }

        return builder.addFormDataPart(
            "file",
            file.name,
            file.asRequestBody("multipart/form-data".toMediaType())
        ).build()
    }

    /**
     * 未加密的 ResponseBody
     * @param any String  解密后的数据
     * @return ResponseBody
     */
    fun getResponseBody(any:String): ResponseBody  = Pattern.compile("\\\\").matcher("\\").replaceAll(any).toResponseBody("application/json; charset=utf-8".toMediaType())

}