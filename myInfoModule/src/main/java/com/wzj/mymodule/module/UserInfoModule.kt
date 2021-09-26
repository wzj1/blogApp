package com.wzj.mymodule.module

import android.app.Application
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.ObservableField
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.Gson
import com.wzj.lib_common.base.repository.BaseRepository
import com.wzj.lib_common.config.RouteConfig
import com.wzj.lib_common.entitys.BaseData
import com.wzj.lib_common.entitys.BaseResult
import com.wzj.lib_common.entitys.LoginEntity
import com.wzj.lib_common.retrofit.HttpBuilder
import com.wzj.lib_common.retrofit.ToRequestBody
import com.wzj.lib_common.sp.SPUtil
import com.wzj.lib_common.util.ApplacationContextHolder
import com.wzj.mymodule.R
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.alpha.XUIAlphaTextView
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView
import com.xuexiang.xui.widget.picker.widget.TimePickerView
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder
import com.xuexiang.xutil.data.DateUtils
import com.ydph.lib_base.http.callback.HttpCallback
import com.ydph.lib_common.log.Logs
import me.goldze.mvvmhabit.utils.ToastUtils
import java.text.SimpleDateFormat
import java.util.*

class UserInfoModule:BaseRepository, HttpCallback<Any?>{
    constructor(application: Application):super(application){}

    private var timePickerBuilder: TimePickerView? = null

    var myUserInfo = ObservableField<LoginEntity>(SPUtil.instance.getUserInfo())

    var userAge:String =myUserInfo.get()?.userAge.toString()

    private var selectYear: String = SimpleDateFormat("yyyy-MM-dd").format(Date())

    /**
     * 星座选择
     * @return OnClickListener
     */
    fun showConstellAtion(btn: XUIAlphaTextView) = View.OnClickListener {
        var constellationSelectOption = 0
      val mConstellationOption =   ResUtils.getStringArray(R.array.constellation_entry)

        val pvOptions: OptionsPickerView<Any> = OptionsPickerBuilder(
            ApplacationContextHolder.mContext
        ) { v: View?, options1: Int, options2: Int, options3: Int ->
           val user = myUserInfo.get()
            user!!.userConstellAtion = mConstellationOption[options1]
            myUserInfo.set(user)
            btn.text = mConstellationOption[options1]
            constellationSelectOption = options1
            false
        }
            .setTitleText("星座选择")
            .setSelectOptions(constellationSelectOption)
            .build()
        pvOptions.setPicker(mConstellationOption)
        pvOptions.show()
    }


    /**
     * 性别选择
     */
     fun showSexPickerView(btn: XUIAlphaTextView) = View.OnClickListener {
        var constellationSelectOption = 0
        val mConstellationOption =   ResUtils.getStringArray(R.array.sex_option)
        val pvOptions: OptionsPickerView<Any> = OptionsPickerBuilder(
            ApplacationContextHolder.mContext
        ) { v: View?, options1: Int, options2: Int, options3: Int ->

            val user = myUserInfo.get()
            user!!.userConstellAtion = mConstellationOption[options1]
            myUserInfo.set(user)
            constellationSelectOption = options1
            false
        }
            .setTitleText("性别选择")
            .setSelectOptions(constellationSelectOption)
            .build()
        pvOptions.setPicker(mConstellationOption)
        pvOptions.show()
    }


    /**
     * 退出登录
     */
    fun outLogin(){
        val deferred = HttpBuilder().apiOkHttpCreate?.outLogin()!!
        post(deferred, this)
    }

    override fun onSuccess(data: Any?) {
        Logs.d(Gson().toJson(data).toString())
        ToastUtils.showShort(Gson().toJson(data))
        SPUtil.instance.putValue(SPUtil.SP_USERINFO, "")
        SPUtil.instance.putValue(SPUtil.SP_TOKEN, "")
        ARouter.getInstance().build(RouteConfig.ROUTE_LOGIN).navigation()
        finish()
    }

    override fun failure(statusCode: Int, e: String?) {
        Logs.d("statusCode:$statusCode", e)
        ToastUtils.showShort("code:${statusCode} \r\n msg:${e}")

    }

    /**
     * 修改信息
     */
    fun upUserInfo(){
        val map = hashMapOf<String, Any?>()
        map["userId"] = myUserInfo.get()?.userId
        map["userName"] = myUserInfo.get()?.userName
        map["userNickName"] = myUserInfo.get()?.userNickName
        map["userAge"] = myUserInfo.get()?.userAge
        map["userPhone"] = myUserInfo.get()?.userPhone
        map["userBirthDay"] = myUserInfo.get()?.userBirthDay
        map["userConstellAtion"] = myUserInfo.get()?.userConstellAtion
        map["userEmail"] = myUserInfo.get()?.userEmail

        val deferred = HttpBuilder().apiOkHttpCreate?.upUserInfo(ToRequestBody.getRequestBody(map))
        post1(deferred!!, object : HttpCallback<BaseData<LoginEntity?>?> {
            override fun onSuccess(data: BaseData<LoginEntity?>?) {
                if (data === null) return
                if (BaseResult.getResult(data)!=null) {
                    refreshUserInfo()
                }
            }

            override fun failure(statusCode: Int, e: String?) {
                ToastUtils.showShort("code:$statusCode \r\n msg:$e")
                Logs.d("code:$statusCode \r\n msg:$e")
            }

        })

    }

    /**
     * 刷新信息
     */
    fun refreshUserInfo(){
        val map = hashMapOf<String, Any?>()
        map["userId"] = myUserInfo.get()?.userId
        val deferred = HttpBuilder().apiOkHttpCreate?.queryUserInfo(
            ToRequestBody.getRequestBodyAny(
                map
            )
        )
        post1(deferred!!, object : HttpCallback<BaseData<LoginEntity?>?> {
            override fun onSuccess(data: BaseData<LoginEntity?>?) {
                if (data === null) return
                if (BaseResult.getResult(data)==null) return
                val data = data.data
                Logs.d("${Gson().toJson(data)}")
                val get = myUserInfo.get()
                get?.userName = data?.userName
                get?.userNickName = data?.userNickName
                get?.userAge = if (data?.userAge == null) 0 else data?.userAge!!
                get?.userPhone = data?.userPhone
                get?.userBirthDay = data?.userBirthDay
                get?.userConstellAtion = data?.userConstellAtion
                get?.userEmail = data?.userEmail
                SPUtil.instance.putValue(SPUtil.SP_USERINFO, Gson().toJson(get))
                myUserInfo.set(get)
            }

            override fun failure(statusCode: Int, e: String?) {
                ToastUtils.showShort("code:$statusCode \r\n msg:$e")
                Logs.d("code:$statusCode \r\n msg:$e")
            }

        })

    }



    /**
     * 显示时间dialog
     * @param view TextView? 需要显示结果的View
     */
    fun timePicker(view: TextView?) {

        if (timePickerBuilder == null) {
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            val selectDate = Calendar.getInstance()

            startDate.time = DateUtils.string2Date("1900-12-31", DateUtils.yyyyMMdd.get())
            endDate.time = DateUtils.string2Date("2100-12-31", DateUtils.yyyyMMdd.get())
            selectDate.time = DateUtils.string2Date("1999-12-31", DateUtils.yyyyMMdd.get())
            timePickerBuilder = TimePickerBuilder(ApplacationContextHolder.mContext) { date, v ->
//                val year = DateUtils.getYear(date)
//                selectYear = DateUtils.getYear(date).toString()
//                selectYear = DateUtils.get(date).toString()
                view?.text =  DateUtils.date2String(date,DateUtils.yyyyMMdd.get())
                val user = myUserInfo.get()
                user!!.userConstellAtion = DateUtils.date2String(date,DateUtils.yyyyMMdd.get())
                myUserInfo.set(user)

            }
                .setRangDate(startDate, endDate)
                .setType(true, true, true, false, false, false)
                .setLabel("年", "月", "日", "", "", "")
                .setDate(selectDate)
                .setOutSideCancelable(false)
                .setCancelColor(Color.parseColor("#A0A0A0"))
                .setTitleText("请选择日期")
                .setContentTextSize(16)
                .setTitleSize(15)
                .setSubCalSize(15)
                .build()
        }
        timePickerBuilder?.show()
    }


}