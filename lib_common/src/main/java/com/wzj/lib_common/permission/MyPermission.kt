/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wzj.lib_common.permission

import android.content.Context
import java.util.*
import com.wzj.lib_common.R

/**
 *
 * Permissions.
 * Created by YanZhenjie on 2017/8/4.
 */
object MyPermission {
    const val READ_CALENDAR = "android.permission.READ_CALENDAR"
    const val WRITE_CALENDAR = "android.permission.WRITE_CALENDAR"
    const val CAMERA = "android.permission.CAMERA"
    const val READ_CONTACTS = "android.permission.READ_CONTACTS"
    const val WRITE_CONTACTS = "android.permission.WRITE_CONTACTS"
    const val GET_ACCOUNTS = "android.permission.GET_ACCOUNTS"
    const val ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"
    const val ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION"
    const val RECORD_AUDIO = "android.permission.RECORD_AUDIO"
    const val READ_PHONE_STATE = "android.permission.READ_PHONE_STATE"
    const val CALL_PHONE = "android.permission.CALL_PHONE"
    const val READ_CALL_LOG = "android.permission.READ_CALL_LOG"
    const val WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG"
    const val ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL"
    const val USE_SIP = "android.permission.USE_SIP"
    const val PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS"
    const val BODY_SENSORS = "android.permission.BODY_SENSORS"
    const val SEND_SMS = "android.permission.SEND_SMS"
    const val RECEIVE_SMS = "android.permission.RECEIVE_SMS"
    const val READ_SMS = "android.permission.READ_SMS"
    const val RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH"
    const val RECEIVE_MMS = "android.permission.RECEIVE_MMS"
    const val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
    const val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"

    /**
     * Turn permissions into text.
     */
    fun transformText(context: Context, vararg permissions: String): List<String?> {
        return transformText(context, Arrays.asList(*permissions))
    }

    /**
     * Turn permissions into text.
     */
    fun transformText(context: Context, vararg groups: Array<String>): List<String?> {
        val permissionList: MutableList<String> = ArrayList()
        for (group in groups) {
            permissionList.addAll(Arrays.asList(*group))
        }
        return transformText(context, permissionList)
    }

    /**
     * Turn permissions into text.
     */
    fun transformText(context: Context, permissions: List<String>): List<String> {
        val textList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            when (permission) {
                READ_CALENDAR, WRITE_CALENDAR -> {
                    val message = context.getString(R.string.my_permission_name_calendar)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                CAMERA -> {
                    val message = context.getString(R.string.my_permission_name_camera)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS -> {
                    val message = context.getString(R.string.my_permission_name_contacts)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION -> {
                    val message = context.getString(R.string.my_permission_name_location)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                RECORD_AUDIO -> {
                    val message = context.getString(R.string.my_permission_name_microphone)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, USE_SIP, PROCESS_OUTGOING_CALLS -> {
                    val message = context.getString(R.string.my_permission_name_phone)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                BODY_SENSORS -> {
                    val message = context.getString(R.string.my_permission_name_sensors)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS -> {
                    val message = context.getString(R.string.my_permission_name_sms)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE -> {
                    val message = context.getString(R.string.my_permission_name_storage)
                    if (!textList.contains(message)) {
                        textList.add(message)
                    }
                }
                else -> if (!textList.contains(permission)) {
                    textList.add(permission)
                }
            }
        }
        return textList
    }

    object Group {
        val CALENDAR = arrayOf(
            READ_CALENDAR,
            WRITE_CALENDAR
        )
        val CAMERA = arrayOf(MyPermission.CAMERA)
        val CONTACTS = arrayOf(
            READ_CONTACTS,
            WRITE_CONTACTS,
            GET_ACCOUNTS
        )
        val LOCATION = arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION
        )
        val MICROPHONE = arrayOf(RECORD_AUDIO)
        val PHONE = arrayOf(
            READ_PHONE_STATE,
            CALL_PHONE,
            READ_CALL_LOG,
            WRITE_CALL_LOG,
            ADD_VOICEMAIL,
            USE_SIP,
            PROCESS_OUTGOING_CALLS
        )
        val SENSORS = arrayOf(BODY_SENSORS)
        val SMS = arrayOf(
            SEND_SMS,
            RECEIVE_SMS,
            READ_SMS,
            RECEIVE_WAP_PUSH,
            RECEIVE_MMS
        )
        val STORAGE = arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )
    }
}