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

package com.adityaamolbavadekar.hiphe.mail

import android.content.Context
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class SendMailTo(
    private val context: Context,
    private val mailTo: String,
    private val mailToUserName: String
) {

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var body1: String
    fun send() {
        val firestore = Firebase.firestore
        firestore.collection("CRED")
            .document("TH")
            .get()
            .addOnSuccessListener {
                username = it.data?.get("username").toString()
                password = it.data?.get("password").toString()
                body1 = it.data?.get("message").toString()
            }

        val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        val message = MimeMessage(session)
        val body =
            "Hey $mailToUserName,\n$body1\n\nThis is link to your profile : https://hiphe.page.link/user/${
                mailTo.removeSuffix(
                    "@gmail.com"
                )
            }"
        message.setFrom(InternetAddress(context.getString(R.string.support_emailaddress)))
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo))
        message.subject = "Welcome To Hiphe"
        message.setText(body)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Transport.send(message)
            } catch (e: Exception) {
                HipheErrorLog("SendMailTo", "Error Sending email", e.toString())
            }
        }

    }

}