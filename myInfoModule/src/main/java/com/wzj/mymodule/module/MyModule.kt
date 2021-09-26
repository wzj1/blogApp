package com.wzj.mymodule.module

import android.app.Application
import android.widget.ImageView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.wzj.blog.myblog.entity.ImageEntity
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.config.UrlConfig
import com.wzj.lib_common.entitys.BaseData
import com.wzj.lib_common.entitys.LoginEntity
import com.wzj.lib_common.http.header.BaseHeader
import com.wzj.lib_common.retrofit.HttpBuilder
import com.wzj.lib_common.retrofit.ToRequestBody
import com.wzj.lib_common.sp.SPUtil
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.lib_common.util.PicUtil
import com.wzj.mymodule.R
import com.ydph.lib_base.http.callback.HttpCallback
import com.ydph.lib_common.log.Logs
import me.goldze.mvvmhabit.utils.ToastUtils
import java.io.File

class MyModule:BaseRepository, HttpCallback<ImageEntity?> {
    constructor(application: Application):super(application)


    var name = "这是个人信息中心"


    /**
     * 展示头像
     * @param view ImageView
     */
    fun setImage(view: ImageView){
        if(SPUtil.instance.getUserInfo().userProFilePhoto.isNullOrBlank()) return
        //展示图片  设置图片加载时设置
        val queue = RequestOptions()
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher).diskCacheStrategy(
                DiskCacheStrategy.AUTOMATIC
            )
      val login =SPUtil.instance.getUserInfo()
        //使用glide 加载图片
        Glide.with(view).setDefaultRequestOptions(queue)
            .load(login.userProFilePhoto).into(
                view
            )
    }

    /**
     * 打开相册或拍照 上传头像
     * @param activity Activity
     */
    fun openImage(){

        PicUtil.getPictureSelector(ApplacationContextHolder.activity).forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: MutableList<LocalMedia>?) {

                Logs.d(TAG, "onResult:" + Gson().toJson(result))
                var path: String? = null
                path =
                    if (result!![0].path.startsWith("/content") || result!![0].path.startsWith("content")) {
                        result!![0].realPath
                    } else {
                        result!![0].path
                    }

//                var uri = Uri.fromFile(File(path))
//                uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    /*7.0以上要通过FileProvider将File转化为Uri*/
//                    val aplicationId = activity.applicationInfo.packageName
//                    FileProvider.getUriForFile(activity, "$aplicationId.FileProvider", File(path))
//                } else {
//                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
//                    Uri.fromFile(File(path))
//                }


                val bodyMap = hashMapOf<String, Any?>()
                bodyMap["user_id"] = SPUtil.instance.getUserInfo().userId
                bodyMap["image_type"] =0

                val fileBody =ToRequestBody.getRequestBodyFrom(File(path))
                val deferred = HttpBuilder(UrlConfig.baseUrl,false).apiOkHttpCreate?.uploadImg(fileBody,Gson().toJson(bodyMap))!!
                post1(deferred,this@MyModule)

            }

            override fun onCancel() {

            }

        })


    }


    fun toUserInfo(){
        ARouter.getInstance().build(RouteConfig.ROUTE_MY_USERINFO).navigation()
    }

    override fun onSuccess(data: ImageEntity?) {

        if (data==null) return

        Logs.d(Gson().toJson(data).toString())

        ToastUtils.showShort(Gson().toJson(data))
    }

    override fun failure(statusCode: Int, e: String?) {
        Logs.d("statusCode:$statusCode", e)
        ToastUtils.showShort("code:${statusCode} \r\n msg:${e}")

    }


}