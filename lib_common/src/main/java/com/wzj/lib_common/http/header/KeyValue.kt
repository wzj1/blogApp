package com.wzj.lib_common.http.header

open class KeyValue{
    lateinit  var key:String
    var value:String?=null
    constructor(key: String,value: String?){
        this.key = key
        this.value = value
    }
}