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
import com.adityaamolbavadekar.hiphe.createDynamicLink
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import com.adityaamolbavadekar.hiphe.interaction.UserDeviceData
import com.adityaamolbavadekar.hiphe.interaction.showLongToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance

class UploaderService : Service() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        FirebaseApp.initializeApp(this)
        firestore = Firebase.firestore
        auth = Firebase.auth


        val userEmail = auth.currentUser?.email.toString()
        val userName = auth.currentUser?.displayName.toString()
        val creationTimestamp = auth.currentUser?.metadata?.creationTimestamp
        val photoURL = auth.currentUser?.photoUrl

        val newTraceElement = Firebase.performance.newTrace("service_test_trace")
        newTraceElement.start()
//        val dataGetterThread = Thread {
/*
        val newLogcatRecorderThread = Thread() {
            try {
                val process = Runtime.getRuntime().exec("logcat -d")
                val bfr = BufferedReader(InputStreamReader(process.inputStream))
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    val producedLog: String = "\n\n BEGIN LOGCAT >>\n\n" + bfr.lines()
                        .toString() + "\n\n" + "<< END LOGCAT\n\n"
                    val logc = hashMapOf(
                        "LOGCAT" to "$producedLog ",
                        "LOGGED IN USER? :" to "$userName ",
                        "USER EMAIL : " to "$userEmail "
                    )
                    firestore.collection("usersDeviceData/LOGCAT")
                        .document(userEmail)
                        .set(logc)
                        .addOnSuccessListener {
                        }
                }
//            while (line != null) {
//                endLog.append(line)
//            }
            } catch (e: Exception) {
                e.toString()
            }
        }
        newLogcatRecorderThread.name = "newLogcatRecorderThread"*/

        val data = UserDeviceData().userDeviceDataHashmap(this)
        firestore.collection("usersDeviceData")
            .document(userEmail)
            .set(data)
            .addOnSuccessListener {
                HipheInfoLog(TAG, "firestore.collection(\"usersDeviceData\").addOnSuccessListener")

                val userDataNameEmail = hashMapOf(
                    "username" to userEmail,
                    "emailAddress" to userEmail,
                    "name" to userName,
                    "createdOn" to creationTimestamp,
                    "photoURL" to "$photoURL"
                )

                firestore.collection("users")
                    .document(userEmail)
                    .set(userDataNameEmail)
                    .addOnSuccessListener {
                        HipheInfoLog(TAG, "firestore.collection(\"users\").addOnSuccessListener")

                        val deeplink = createDynamicLink(userEmail)
                        val dpData = hashMapOf(
                            "USER" to userEmail,
                            "USER_DEEPLINK" to "$deeplink"
                        )
                        firestore.collection("deepLinks")
                            .document(userEmail)
                            .set(dpData)
                            .addOnSuccessListener {
                                HipheInfoLog(
                                    TAG,
                                    "firestore.collection(\"deepLinks\").addOnSuccessListener"
                                )
                            }

                    }
            }
            .addOnFailureListener {
                this.showLongToast("Error : $it")
            }

        return START_STICKY

    }

    companion object {
        const val TAG = "UploaderService"
    }

}