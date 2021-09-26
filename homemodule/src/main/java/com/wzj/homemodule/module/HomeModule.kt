package com.wzj.homemodule.module

import android.app.Application
import com.wzj.lib_common.base.repository.BaseRepository

class HomeModule:BaseRepository {
    constructor(application: Application):super(application)



    var name = "这是首页"
}