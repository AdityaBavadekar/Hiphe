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

package com.adityaamolbavadekar.hiphe

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.adityaamolbavadekar.hiphe.interaction.showLongToast
import com.adityaamolbavadekar.hiphe.interaction.userDeviceDataHashmap
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.performance

class DynamicLinkUploaderService : Service() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        FirebaseApp.initializeApp(this)
        firestore = Firebase.firestore
        auth = Firebase.auth


        val userEmail = auth.currentUser?.email.toString()
        val userName = auth.currentUser?.displayName.toString()

        val newTraceElement = Firebase.performance.newTrace("service_test_trace")
        newTraceElement.start()

        val data = userDeviceDataHashmap(this)
        firestore.collection("usersDeviceData")
                .document(userEmail)
                .set(data)
                .addOnSuccessListener {

                    val userDataNameEmail = hashMapOf(
                            "Username" to userEmail,
                            "Email Address" to userEmail,
                            "Name" to userName
                    )

                    firestore.collection("users")
                            .document(userEmail)
                            .set(userDataNameEmail)
                            .addOnSuccessListener {


                                val userProfileLink = createDynamicLink(userEmail)
                                val deepLinkData = hashMapOf(
                                        "USER" to userEmail,
                                        "USER_PROFILE_LINK" to "$userProfileLink"
                                )
                                firestore.collection("usersDeepLinks")
                                        .document(userEmail)
                                        .set(deepLinkData)
                                        .addOnSuccessListener {

                                            firestore.collection("users/DeepLinks")
                                                    .document(userEmail)
                                                    .set(deepLinkData)
                                                    .addOnSuccessListener {
                                                    }
                                        }

                            }
                }
                .addOnFailureListener {
                    this.showLongToast("Error : $it")
                }





        return START_STICKY

    }

}