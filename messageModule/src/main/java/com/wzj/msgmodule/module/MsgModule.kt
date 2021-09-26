package com.wzj.msgmodule.module

import android.app.Application
import com.wzj.lib_common.base.repository.BaseRepository

class MsgModule:BaseRepository {
    constructor(application: Application):super(application)


    var name = "这是消息管理界面"
}