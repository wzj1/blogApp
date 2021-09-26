package com.wzj.lib_common.handler

import android.os.Handler
import android.os.Looper

class MainHandler : Handler(Looper.getMainLooper()) {

    companion object {
        private var instance: MainHandler? = null

        fun getInstance(): MainHandler {

            if (instance == null) {

                synchronized(MainHandler::class.java) {
                    if (instance == null) {
                        instance = MainHandler()
                    }
                }
            }

            return instance!!
        }
    }
}