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

package com.adityaamolbavadekar.hiphe.network

import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import java.net.InetSocketAddress
import java.net.Socket

//FROM TUTORIAL BY CODING-WITH-MITCH
object DoesNetworkHaveInternet {

    fun execute(): Boolean {
        return try {
            HipheInfoLog(TAG, "PINGING google.")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            HipheInfoLog(TAG, "PING success.")
            true
        } catch (e: Exception) {
            HipheErrorLog(TAG, "No Internet connection.", e.toString())
            false
        }
    }

    const val TAG = "DoesNetworkHaveInternet"
}
