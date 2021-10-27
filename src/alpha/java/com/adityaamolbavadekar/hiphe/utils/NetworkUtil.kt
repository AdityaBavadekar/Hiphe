/*******************************************************************************
 * Copyright (c) 2021. Aditya Bavadekar
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 ******************************************************************************/

package com.adityaamolbavadekar.hiphe.utils

import android.content.Context
import android.net.ConnectivityManager


object NetworkUtil {
    fun getConnectivityStatus(context: Context): Int {
        val cm = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (null != activeNetwork) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) return constants.TYPE_WIFI
            if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) return constants.TYPE_MOBILE
        }
        return constants.TYPE_NOT_CONNECTED

    }

    fun getConnectivityStatusString(context: Context): String? {
        val conn = getConnectivityStatus(context)
        var status: String? = null
        if (conn == constants.TYPE_WIFI) {
            status = "Wifi enabled"
        } else if (conn == constants.TYPE_MOBILE) {
            status = "Mobile data enabled"
        } else if (conn == constants.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet"
        }
        return status
    }
}