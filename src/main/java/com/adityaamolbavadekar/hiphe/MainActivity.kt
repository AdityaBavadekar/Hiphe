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

import android.accounts.AccountManager
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.adityaamolbavadekar.hiphe.interaction.*
import com.adityaamolbavadekar.hiphe.network.ConnectionLiveData
import com.adityaamolbavadekar.hiphe.room.CrashDatabase
import com.adityaamolbavadekar.hiphe.services.UploaderService
import com.adityaamolbavadekar.hiphe.utils.ConfigureTheme
import com.adityaamolbavadekar.hiphe.utils.constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var TEXT_TO_SPEECH_INITIALISED: Boolean = false
    private var txt2Sp: TextToSpeech? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var networkStateCardView: CardView
    private lateinit var networkStateTextView: TextView
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConfigureTheme().configureThemeOnCreate(this, TAG)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        firestore = Firebase.firestore

        connectionLiveData = ConnectionLiveData(this)
        auth = Firebase.auth
        if (!prefs.getBoolean("SENT", false)) {
            startService(Intent(this, UploaderService::class.java))
        }

        networkStateCardView = findViewById(R.id.offlineNotifierCardMain)
        networkStateTextView = findViewById(R.id.offlineNotifierCardTextViewMain)
        connectionLiveData.observeForever { isConnectedToNetwork ->
            NotifyNetworkInfo().notifyNetworkMode(
                isConnectedToNetwork,
                this,
                networkStateCardView,
                networkStateTextView
            )
        }

        val isVisitor = prefs.getBoolean(constants.isUserUnexplored, false)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            selectImageFromAlbum()
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_account,
                R.id.nav_about,
                R.id.nav_settings,
                R.id.nav_dashboard

            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        if (isVisitor) {
            HipheInfoLog(
                TAG,
                getString(R.string.user_is_a_newcomer_asking_him_if_he_wants_to_be_guided)
            )
            val b = MaterialAlertDialogBuilder(this)
            b.setIcon(R.drawable.ic_twotone_important_devices_24)
            b.setTitle(getString(R.string.visitor_welcome_to_hiphe))
            b.setMessage(getString(R.string.welcome_it_is_our_pleasure_to_see_you_dialog_message))
            b.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                HipheInfoLog(TAG, getString(R.string.User_AGREED_to_take_a_tour))
                dialogInterface.dismiss()
                takeHipheTour()
                prefs.edit {
                    putBoolean(constants.isUserUnexplored, false)
                }
                //TODO("GUIDE USER TO USE APPLICATION")
            }
            b.setNegativeButton(getString(R.string.no_thanks)) { dialogInterface, _ ->
                HipheInfoLog(TAG, getString(R.string.User_DISAGREED_to_take_a_tour))
                prefs.edit {
                    putBoolean(constants.isUserUnexplored, false)
                }
                dialogInterface.dismiss()

            }
            b.setCancelable(false)
            b.create()
            b.show()
        }

        upload()
    }

    private fun takeHipheTour() {

    }

    private fun upload() {

        Thread {
            val crashDatabase = Room.databaseBuilder(
                this,
                CrashDatabase::class.java,
                "NOTES_REGISTRY"
            ).build()
            crashDatabase.crashDao().getCrashes()

            try {
                connectionLiveData.observe(this, androidx.lifecycle.Observer {
                    while (it == true) {
                        val crashes = crashDatabase.crashDao().getCrashes()
                            .observe(this, androidx.lifecycle.Observer { crashList ->
                                for (crash in crashList) {
                                    val crashHashMAP = hashMapOf(
                                        "INDEX" to crash.index.toString(),
                                        "CRASH_MAIN_DATA" to crash.crashMainData.toString(),
                                        "CRASH_TIMESTAMP" to crash.timestamp.toString()
                                    )
                                    firestore.collection("CRASH_DATABASE")
                                        .add(crashHashMAP)
                                        .addOnSuccessListener {
                                        }
                                }
                            })
                    }
                })

            } catch (e: Exception) {
                HipheErrorLog(
                    UploaderService.TAG,
                    "Error connectionLiveData.observeForever >> Service",
                    e.toString()
                )
            }

        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            R.id.action_logout -> {
                val b = MaterialAlertDialogBuilder(this)//.Builder(requireActivity())
                b.setTitle("Are you sure?")
                b.setMessage(getString(R.string.are_you_sure_logout_message))
                b.setPositiveButton(getString(R.string.logout)) { ba, _ ->
                    DeleteChannels.Builder(this).deleteChannelGroup(
                        CreateChannels.ChannelGroup(
                            auth.currentUser!!.email!!,
                            auth.currentUser!!.email!!
                        )
                    )
                    auth.signOut()
                    try {
                        googleSignInClient.revokeAccess()
                        auth.signOut()

                    } catch (e: Exception) {
                    }
                    PreferenceManager.getDefaultSharedPreferences(this).edit {
                        putBoolean(constants.checkIsLoggedInPrefKey, false)
                    }
                    HipheInfoLog(
                        TAG,
                        getString(R.string.User_is_logging_out_without_remembrance__signuot)
                    )
                    startActivity(Intent(this, LauncherActivity::class.java))
                    ba.dismiss()
                    finish()
                }
                b.setNegativeButton(getString(R.string.remember_logout)) { ba, _ ->
                    auth.signOut()
                    try {
                        googleSignInClient.revokeAccess()
                        auth.signOut()

                    } catch (e: Exception) {
                    }
                    PreferenceManager.getDefaultSharedPreferences(this).edit {
                        putBoolean(constants.checkIsLoggedInPrefKey, false)
                    }
                    HipheInfoLog(
                        TAG,
                        getString(R.string.User_is_logging_out_with_remembrance__signuot)
                    )
                    startActivity(Intent(this, LauncherActivity::class.java))
                    ba.dismiss()
                    finish()
                }
                b.setNeutralButton(getString(R.string.cancel)) { ba, _ ->
                    ba.dismiss()
                }
                b.setCancelable(false)
                b.create()
                b.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getAccounts(): String? {
        return if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.GET_ACCOUNTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val accountManager =
                this.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
            val accounts = accountManager.accounts
            var emailID = ""
            for (account in accounts) {
                emailID += "\n" + account.name
            }
            emailID
        } else null
    }

    private fun imgLabeling(imageUri: Uri) {
        selectImageFromAlbum()
        ImageView(this).setImageURI(imageUri)
        val image = InputImage.fromFilePath(this, imageUri)
        val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
        labeler.process(image)
            .addOnSuccessListener { labels ->
                var resultingText = ""
                for (label in labels) {
                    val text = label.text
                    val confidence = label.confidence
                    val index = label.index
                    resultingText += "\n****\n Guess : $text \n Confidence : $confidence\n Index : $index\n"
                }
                showAlertDialogImageLabelingResult(
                    resultingText,
                    imageUri,
                    labels[0].text,
                    labels[0].confidence,
                )
            }
            .addOnFailureListener {

            }
    }

    private fun showAlertDialogImageLabelingResult(
        resultingText: String,
        imageUri: Uri,
        firstGuess: String,
        firstGuesssConfidence: Float
    ) {
        val d = Dialog(this)
        d.requestWindowFeature(Window.FEATURE_NO_TITLE)
        d.setContentView(R.layout.custom_dialog_for_image_labeling)
        d.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(d.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        d.findViewById<TextView>(R.id.mainResultTextView).text = resultingText
        try {
            d.findViewById<ImageView>(R.id.mainImageView).setImageURI(imageUri)
        } catch (e: Exception) {
        }
        d.findViewById<Button>(R.id.laert_great_button).setOnClickListener { d.dismiss() }
        d.show()
        d.window!!.attributes = lp


        val speak = getString(
            R.string.first_guess_with_confidence_level_of,
            firstGuess,
            firstGuesssConfidence.toString()
        )
        val accountInfo = getAccounts()
        if (accountInfo != null) {
            val acSpeak =
                "hey $accountInfo " + getString(
                    R.string.first_guess_with_confidence_level_of,
                    firstGuess,
                    firstGuesssConfidence.toString()
                )
            txt2Sp = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    TEXT_TO_SPEECH_INITIALISED = true
                    speakThis(acSpeak)
                }
            }
        } else {
            txt2Sp = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    TEXT_TO_SPEECH_INITIALISED = true
                    speakThis(speak)
                }
            }
        }
    }

    private fun speakThis(textToSpeak: String) {
        if (TEXT_TO_SPEECH_INITIALISED && txt2Sp != null) {
            val txt2SpResult = txt2Sp!!
            txt2SpResult.language = Locale.US
            txt2SpResult.speak(
                textToSpeak,
                TextToSpeech.QUEUE_FLUSH,
                null,
                Random(100).toString() + java.util.Random().toString() + System.currentTimeMillis()
                    .toString()
            )
            if (!txt2SpResult.isSpeaking) txt2Sp!!.shutdown()
        }
    }

    private fun selectImageFromAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imgLabelingLauncher.launch(intent)
    }

    private var imgLabelingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                if (data?.data != null) {
                    val dataUri = data.data
                    if (dataUri != null) {
                        imgLabeling(dataUri)
                        return@registerForActivityResult
                    }
                }
            }
        }

    override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        if (auth.currentUser == null) {
            PreferenceManager.getDefaultSharedPreferences(this).edit {
                putBoolean(constants.checkIsLoggedInPrefKey, false)
            }
            startActivity(Intent(this, LauncherActivity::class.java))
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }


    ///OFFLINE-ONLINE HANDELING

    ///OFFLINE-ONLINE HANDELING

}