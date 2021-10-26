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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.adityaamolbavadekar.hiphe.ui.dashboard.HomeMainViewModel
import com.adityaamolbavadekar.hiphe.ui.home.HomeViewModel
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.about_hiphe_fragment.*

class HipheInformationActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private lateinit var usernameTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var joinedDateTextView: TextView
    private lateinit var photoImageView: ImageView
    private lateinit var mainCardView: CardView
    private lateinit var mainCardViewError: CardView
    private lateinit var hipheInformationViewModel: HipheInformationViewModel

    class HipheInformationViewModel : ViewModel() {

        private var _emailAddress = MutableLiveData<String>().apply {
            value = "Email Address : "
        }
        var emailAddress: LiveData<String> = _emailAddress
        fun setEmailAddress(EmailAddress: String) {
            _emailAddress.value = EmailAddress
        }


        private var _name = MutableLiveData<String>().apply {
            value = "Name : "
        }
        var name: LiveData<String> = _name
        fun setName(name: String) {
            _name.value = name
        }

        private var _joinedDate = MutableLiveData<String>().apply {
            value = "Joined : "
        }
        var joinedDate: LiveData<String> = _joinedDate
        fun setJoinedDate(joinedDate: String) {
            _joinedDate.value = joinedDate
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_hiphe_fragment)
        setSupportActionBar(toolbar)

        hipheInformationViewModel =
                ViewModelProviders.of(this).get(HipheInformationViewModel::class.java)
        progressBar = findViewById(R.id.progressBar)
        usernameTextView = findViewById(R.id.usernameTextView)
        nameTextView = findViewById(R.id.nameTextView)
        joinedDateTextView = findViewById(R.id.creationDateTextView)
        photoImageView = findViewById(R.id.accountImageImageView)
        mainCardView = findViewById(R.id.mainCardView)
        mainCardViewError = findViewById(R.id.mainCardViewError)

        progressBar.visibility = View.VISIBLE
        handleIntentMain(intent)

        hipheInformationViewModel.emailAddress.observe(this, Observer {
            usernameTextView.text = it
        })

        hipheInformationViewModel.name.observe(this, Observer {
            nameTextView.text = it
        })

        hipheInformationViewModel.joinedDate.observe(this, Observer {
            joinedDateTextView.text = it
        })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntentMain(intent)
    }

    private fun handleIntentMain(intent: Intent?) {

        try {
            val action = intent?.action
            val data = intent?.data

            if (Intent.ACTION_VIEW == action) {
                data?.lastPathSegment?.also { userEmailId ->
                    HipheInfoLog(TAG, "Data obtained from link(userEmailId) : $userEmailId")
                    Uri.parse("https://hiphe.page.link/")
                            .buildUpon()
                            .appendPath(userEmailId)
                            .build().also { appData ->
                                this.showToast("$appData")
                                HipheInfoLog(TAG, "Data obtained from link(appData) : $appData")
                                val fullEmailAddress = "$userEmailId@gmail.com"
                                HipheInfoLog(TAG, "Data combined(fullEmailAddress) : $fullEmailAddress")

                                firestore = Firebase.firestore
                                firestore.collection("users")
                                        .document(fullEmailAddress)
                                        .get()
                                        .addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                val user = document.toObject(HipheUserClass::class.java)!!
                                                HipheDebugLog(
                                                        TAG,
                                                        "Successfully Found User Document and Converted to HipheUserClass : $user"
                                                )
                                                HipheInfoLog(TAG, "DOCUMENT FOUND : $document")
                                                showUserInfo(
                                                        user.username,
                                                        user.photoURL,
                                                        user.name,
                                                        user.createdOn
                                                )
                                            } else {
                                                HipheErrorLog(TAG, "Document specified does not exists : ", "Unknown Document")

                                                this.showToast("User is invalid")
                                                progressBar.visibility = View.GONE
                                                mainCardViewError.visibility = View.VISIBLE
                                            }
/*
                                    documents.forEach { document ->


                                        val username = document.data["username"].toString()
                                        HipheInfoLog(TAG,"DOCUMENT : $username")
                                        val photoURL = document.data["photoURL"].toString()
                                        val name = document.data["name"].toString()
                                        HipheInfoLog(TAG,"DOCUMENT : $name")
                                        val creationTimestamp =
                                            document.data["createdOn"].toString()

                                        if (username == fullEmailAddress) {
                                            HipheInfoLog(TAG,"DOCUMENT FOUND : $document")
                                            showUserInfo(
                                                username,
                                                photoURL,
                                                name,
                                                creationTimestamp
                                            )
                                        } else {
                                            progressBar.visibility = View.GONE
                                            mainCardViewError.visibility = View.VISIBLE
                                        }
                                    }
*/

                                        }
                                        .addOnFailureListener { e ->
                                            HipheErrorLog(
                                                    TAG,
                                                    "firestore.collection(\"users\").get().addOnSuccessListener { documents ->.addOnFailureListener >>",
                                                    e.toString()
                                            )
                                            this.showToast("$e")
                                            progressBar.visibility = View.GONE
                                            mainCardViewError.visibility = View.VISIBLE
                                        }


                            }
                }
            }
        } catch (e: Exception) {
            HipheErrorLog(
                    TAG,
                    "Error in HipheInformationActivity\$handleIntentMain(intent: Intent?)",
                    e.toString()
            )
            this.showToast("$e")
            progressBar.visibility = View.GONE
            mainCardViewError.visibility = View.VISIBLE
        }
    }

    private fun showUserInfo(
            username: String,
            photoURL: String,
            name: String,
            creationTimestamp: Long
    ) {
        try {
            val photoUri = Uri.parse(photoURL)
            Glide.with(this)
                    .load(photoUri)
                    .into(photoImageView)
        } catch (e: Exception) {
            val photoUri =
                    Uri.parse("https://upload.wikimedia.org/wikipedia/commons/f/f3/Instagram_verifed.png")
            Glide.with(this)
                    .load(photoUri)
                    .into(photoImageView)
        }
        hipheInformationViewModel.setName(name)
        hipheInformationViewModel.setEmailAddress("Email Address : $username")
        hipheInformationViewModel.setJoinedDate("Joined : $creationTimestamp")
        progressBar.visibility = View.GONE
        mainCardView.visibility = View.VISIBLE

    }

    companion object {
        const val TAG = "HipheInformationActivity"
    }

}