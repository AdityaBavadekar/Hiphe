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

package com.adityaamolbavadekar.hiphe.ui.feedback

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.getTheFinalLogs
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class SendFeedbackFragment : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var editText: EditText
    private val logs = getTheFinalLogs()
    private lateinit var sysData: HashMap<String, Any>
    private lateinit var progressBar: ProgressBar
    private lateinit var imageView: ImageView
    private lateinit var imageViewClicker1: ImageView
    private lateinit var imageViewClicker2: ImageView
    private lateinit var materialCheckBox: MaterialCheckBox
    private lateinit var materialCheckBox2: MaterialCheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_send_feedback_layout)
        sysData = getTheData()
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        editText = findViewById(R.id.editText)
        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)
        imageViewClicker1 = findViewById(R.id.ScreenshotCardClick)
        imageViewClicker2 = findViewById(R.id.SytemLogsCardClick)
        materialCheckBox = findViewById(R.id.materialCheckBox)
        materialCheckBox2 = findViewById(R.id.materialCheckBox2)

        imageViewClicker2.setOnClickListener {
            val b = AlertDialog.Builder(this)
            b.setTitle("System Logs")
            b.setMessage("\n\n$sysData")
            b.setPositiveButton("OK, GOT IT") { ba, _ ->
                ba.dismiss()
            }
            b.create()
            b.show()
        }

//        try {
//            if (ScreenShot.bitmap != null) {
//                imageView.setImageBitmap(ScreenShot.bitmap)
//            }
//        } catch (e: Exception) {
//        }

        val userEmail = Firebase.auth.currentUser?.email

        val users = if (userEmail != null) {
            mutableListOf(
                " Hiphe User",
                " $userEmail"
            )
        } else {
            mutableListOf(
                " Hiphe User",
            )
        }
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, users)
        autoCompleteTextView.setAdapter(arrayAdapter)

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    private fun getTheData(): HashMap<String, Any> {
        val manufacturer = Build.MANUFACTURER
        val id = Build.ID
        val device = Build.DEVICE
        val brand = Build.BRAND
        val androidOs = Build.VERSION.SDK_INT
        val product = Build.PRODUCT
        val pckMangr = this.packageManager.getPackageInfo(this.packageName, 0)
        val packageName = pckMangr.packageName
        val versionName = pckMangr.versionName
        val androidVersion = Build.VERSION.RELEASE
        val time = Build.TIME
        val user = Build.USER
        val freeSpace = Environment.getDataDirectory().freeSpace
        val totalSpace = Environment.getDataDirectory().totalSpace
        val timeZone = TimeZone.getDefault().id
        val unknown = Build.UNKNOWN
        val type = Build.TYPE
        val fingerprint = Build.FINGERPRINT
        val hardware = Build.HARDWARE
        val host = Build.HOST
        val display = Build.DISPLAY
        val tags = Build.TAGS
        val model = Build.MODEL
        val timeNow =
            SimpleDateFormat(("EEE, dd mm yyyy, HH:mm aaa Z"), Locale.ENGLISH).format(Date())
                .toString()
        val data = hashMapOf(
            "HIPHE_LOGS" to logs,
            "Feedback" to editText.text.toString(),
            "Date" to SimpleDateFormat("E, dd-mm-yy", Locale.ENGLISH).format(Date()).toString(),
            "Time" to SimpleDateFormat("HH:mm Z", Locale.ENGLISH).format(Date()).toString(),
            "APP NAME" to "Hiphe",
            "USER" to user,
            "TIMEZONE" to timeZone,
            "Fingerprint" to "$fingerprint",
            "Hardware" to "$hardware",
            "Host" to "$host",
            "Display" to "$display",
            "TAGS" to "$tags",
            "MODEL" to "$model",
            "MANUFACTURER" to "$manufacturer",
            "USER" to "$user",
            "ID" to "$id",
            "DEVICE" to "$device",
            "BRAND" to "$brand",
            "PRODUCT" to "$product",
            "Android SDK Version" to "$androidOs",
            "Android Release Version" to "$androidVersion",
            "Free Space" to "$freeSpace",
            "Total Space" to "$totalSpace",
            "Package Name" to "$packageName",
            "Version Name" to "$versionName",
            "CURRENT TIMESTAMP" to "$timeNow"
        )
        return data
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.send_feedback, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_send -> {
                progressBar.visibility = View.VISIBLE
                val data = sysData
                if (editText.text.toString() != "") {
                    data["USER_INPUT"] = editText.text.toString()
                    data["FEEDBACK_SENT_FROM"] = autoCompleteTextView.text.toString()
                    checkForCheckBox(data, editText.text.toString())
                } else {
                    progressBar.visibility = View.GONE
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkForCheckBox(data: HashMap<String, Any>, textInput: String) {

        if (materialCheckBox.isChecked && materialCheckBox2.isChecked) {
            uploadScreenShotAndSystemLogs(data)
        } else if (!materialCheckBox.isChecked && !materialCheckBox2.isChecked) {
            uploadText(textInput)
        } else if (!materialCheckBox.isChecked && materialCheckBox2.isChecked) {
            uploadOnlySystemLogs(data)
        } else if (materialCheckBox.isChecked && !materialCheckBox2.isChecked) {
            uploadOnlyScreenshot(textInput)
        }
    }

    private fun uploadText(data: String) {

        val firestore = Firebase.firestore
        firestore.collection("HIPHE_USER_INITIATED_FEEDBACKs")
            .add(data)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                this.showToast("Your Feedback was recorded, Thankyou")
                finish()
            }
            .addOnFailureListener {
                this.showToast("Something went wrong, please try again ${it.cause}")
                progressBar.visibility = View.GONE
            }
    }

    private fun uploadOnlyScreenshot(data: String) {
        //screenshot not implemented
        uploadText(data)
    }

    private fun uploadOnlySystemLogs(data: HashMap<String, Any>) {
        val firestore = Firebase.firestore
        firestore.collection("HIPHE_USER_INITIATED_FEEDBACKs")
            .add(data)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                this.showToast("Your Feedback was recorded, Thankyou")
                finish()
            }
            .addOnFailureListener {
                this.showToast("Something went wrong, please try again ${it.cause}")
                progressBar.visibility = View.GONE
            }
    }

    private fun uploadScreenShotAndSystemLogs(data: HashMap<String, Any>) {
        //Screenshot upload not implemented so this will default to uploadingSytemLogs
        uploadOnlySystemLogs(data)
    }

}