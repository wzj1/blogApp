package com.wzj.lib_common.titleUtil.base

import com.wzj.lib_common.R

object TopBarHelper {

    fun getDrawableId(str: String?):Int{
        var imgId = 0

        if (str.isNullOrBlank())  return 0

      return  when(str){
          str.equals("back_gray",ignoreCase = true).toString()  ->  0
          str.equals("back_white",ignoreCase = true).toString()  -> 0

          else -> 0

        }

    }

}