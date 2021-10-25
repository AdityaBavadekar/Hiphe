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

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_shared.*

class SharedActivity : AppCompatActivity() {

    private val TAG = "SharedActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared)


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
                }
            }
            intent?.type?.startsWith("video/wav") == true -> {
                this.showToast("Video.wav")
            }
            intent?.type?.startsWith("video/mp4") == true -> {
                this.showToast("Video.mp4")
            }
            else -> {
                this.showToast("Unknown type")
            }
        }

        result_ok_shared_activity.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }





        try {


            val textExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("txt")
            val imagePngExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("png")
            val imageJpgExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpg")
            val imageJpegExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("jpeg")
            val imageGifExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif")
            val imageWebpExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("webp")
            val imageSvgzExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("svgz")
            val videoMp4ExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp4")
            val videoWavExtensionMimeTypeMap =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension("wav")

            val mimeTypeHashmap = hashMapOf(
                    "txt" to "$textExtensionMimeTypeMap",
                    "png" to "$imagePngExtensionMimeTypeMap",
                    "jpg" to "$imageJpgExtensionMimeTypeMap",
                    "jpeg" to "$imageJpegExtensionMimeTypeMap",
                    "gif" to "$imageGifExtensionMimeTypeMap",
                    "webp" to "$imageWebpExtensionMimeTypeMap",
                    "svgz" to "$imageSvgzExtensionMimeTypeMap",
                    "mp4" to "$videoMp4ExtensionMimeTypeMap",
                    "mp4" to "$videoMp4ExtensionMimeTypeMap",
                    "wav" to "$videoWavExtensionMimeTypeMap"
            )

            val firestore: FirebaseFirestore = Firebase.firestore
            firestore.collection("MimeTypes")
                    .document("MimeTypes")
                    .set(mimeTypeHashmap)
                    .addOnSuccessListener {
                    }


        } catch (e: Exception) {
        }


    }


    private fun getContacts() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        Log.i("CONTACT_PROVIDER", "Total of ${cursor?.columnCount} contacts")

        if (cursor?.columnCount!! > 0) {
            while (cursor.moveToNext()) {

                val contactName =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val contactPhone =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
            }
        }
    }


    private fun getDataFromMimeType() {
        val uri = intent?.data

        if (uri != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)

            if (intent?.data != null) {

                intent?.data?.let { returnUri ->
                    val mimeType = contentResolver.getType(returnUri)
                    val cursor1 = contentResolver.query(returnUri, null, null, null, null)
                    if (cursor1 != null) {
                        val nameIndex = cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        val sizeIndex = cursor1.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor1.moveToFirst()


                    }
                }

            }

        }


    }

}