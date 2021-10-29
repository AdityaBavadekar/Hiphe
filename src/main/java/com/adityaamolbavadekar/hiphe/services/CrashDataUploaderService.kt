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

package com.adityaamolbavadekar.hiphe.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.adityaamolbavadekar.hiphe.utils.constants
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrashDataUploaderService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent!= null){

            var crashData = intent.getStringExtra(constants.CRASH_FULL_DATA_VALUE_KEY).toString()
            val crashDataUser = intent.getStringExtra(constants.CRASH_FULL_DATA_USER_KEY).toString()
            crashData += "\n\nUSER : $crashDataUser\n\n"
            val firestore = Firebase.firestore
            firestore.collection("HIPHE_REMOTE_CRASHES")
                .add(crashData)
                .addOnSuccessListener {
                    this.stopSelf()
                }

        }

        return START_STICKY
    }
}