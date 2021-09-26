package com.wzj.lib_common.entitys

open class LoginEntity {

    var friendsData:ArrayList<Any>?=null
    //id
    var userAge:Int = 0
    var userBirthDay:String?=null
    var userConstellAtion:String?=null
    //登录账号
     var userEmail:String? = null
    //登录密码
     var userId:Int = 0
    var userIp:String?=""
    var userName:String?=""
    var userNickName:String?=""
    var userPhone:String?=""
    var userProFilePhoto:String?=""
    var userPwd:String?=""
    var userRegisrAtionTime:String?=""
    //登录类型: 0 手机号 1 邮箱账号 2 用户名  3 其他(暂不支持)
    var login_type:Int= 0
    var token:String?= null
    var status :Int = -1



}