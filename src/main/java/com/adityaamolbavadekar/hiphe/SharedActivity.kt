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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shared.*

class SharedActivity : AppCompatActivity() {

    private val TAG = "SharedActivity"
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared)

        textView = findViewById(R.id.resultTetxView)

        val data: Uri? = intent?.data

        when {
            intent?.type?.startsWith("image/jpeg") == true -> {
                this.showToast("Image.jpeg")
            }
            intent?.type?.startsWith("image/jpg") == true -> {
                this.showToast("Image.jpg")
            }
            intent?.type?.startsWith("image/gif") == true -> {
                this.showToast("Image.gif")
            }
            intent?.type?.startsWith("image/png") == true -> {
                this.showToast("Image.png")
            }
            intent?.type?.startsWith("image/webp") == true -> {
                this.showToast("Image.webp")
            }
            intent?.type?.startsWith("image/svgz") == true -> {
                this.showToast("Image.svgz")
            }
            intent?.type?.startsWith("image/wbmp") == true -> {
                this.showToast("Image.wbmp")
            }
            intent?.type?.startsWith("text/*") == true -> {
                this.showToast("Text")
                if (data != null) {
                    this.showToast("${contentResolver.getType(data)}")
                    val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                    if (intent.getStringExtra(Intent.EXTRA_TITLE) != null) {
                        textView.text = text + intent.getStringExtra(Intent.EXTRA_TITLE).toString()
                    }else{
                        textView.text = text
                    }
                }
            }
            intent?.type?.startsWith("video/wav") == true -> {
                this.showToast("Video.wav")
            }
            intent?.type?.startsWith("video/mp4") == true -> {
                this.showToast("Video.mp4")
            }
            else -> {
                val type = contentResolver.getType(data!!).toString()
                this.showToast("Unknown type: $type , ${data.scheme}")
            }
        }

        result_ok_shared_activity.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}