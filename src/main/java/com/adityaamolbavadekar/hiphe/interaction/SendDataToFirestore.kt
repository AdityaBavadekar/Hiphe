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

package com.adityaamolbavadekar.hiphe.interaction

import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.adityaamolbavadekar.hiphe.LauncherActivity
import com.adityaamolbavadekar.hiphe.network.DoesNetworkHaveInternet
import com.adityaamolbavadekar.hiphe.room.CrashDataClass
import com.adityaamolbavadekar.hiphe.room.CrashDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

class SendDataToFirestore(
    private val context: Context,
    private val value: String,
    private val mainData: String,
    private val fileName: String,
    private val email: String?
) {
    private lateinit var crashDatabase: CrashDatabase

    fun start() {
        Thread {
            crashDatabase = Room.databaseBuilder(
                context,
                CrashDatabase::class.java,
                "NOTES_REGISTRY"
            ).build()
            val timestamp = SimpleDateFormat(
                "EEE, dd-mm-yy, HH:mm aaa Z",
                Locale.ENGLISH
            ).format(Date())
                .toString()
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    crashDatabase.crashDao().commitCrashReport(
                        CrashDataClass(
                            0,
                            "$mainData\n\nUSER : $email\n\n", timestamp
                        )
                    )
                }
            } catch (e: Exception) {
            }
            val isConnected = DoesNetworkHaveInternet.execute()
            if (isConnected) {
                val firestore = Firebase.firestore
                firestore.collection("HIPHE_REMOTE_CRASHES")
                    .document(fileName)
                    .set(returnData())
                restartHipheLauncherActivity(returnData().toString())
            } else {
                restartHipheLauncherActivity(null)
            }
        }.start()
    }

    private fun restartHipheLauncherActivity(data: String?) {
        val intent = Intent(context, LauncherActivity::class.java)
        if (data != null) {
            intent.putExtra("LOGS", data)
        } else {
            intent.putExtra("LOGS", HipheLogger.toString())
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        exitProcess(404)

    }

    private fun returnData(): HashMap<String, String> {
        val TAG = "sendFeedback\$sendCrashData"
        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
        return hashMapOf(
            "USER_EMAIL_ID" to "$email",
            "CRASH_DEVICE_DATA" to mainData,
            "CRASH_MAIN_DATA" to value,
            "CRASH_PATH_TO_SAVED_DATA" to context.filesDir.path,
            "CRASH_FILES_IN_PATH_PARENT" to "${context.fileList().size}",
            "CRASH_HIPHE_LOGS" to "${getTheFinalLogs()}"
        )
    }

}