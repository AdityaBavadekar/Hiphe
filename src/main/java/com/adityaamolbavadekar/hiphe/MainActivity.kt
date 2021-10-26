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

/*******************************************************************************
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 ******************************************************************************/

/*******************************************************************************
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
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
import androidx.appcompat.app.AppCompatDelegate
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
import com.adityaamolbavadekar.hiphe.interaction.notifyNetworkMode
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        try {
            when (prefs.getString("theme", "3")) {
                "1" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    HipheInfoLog(LauncherActivity.TAG, "Initiating MODE_NIGHT_YES")
                }
                "2" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    HipheInfoLog(LauncherActivity.TAG, "Initiating MODE_NIGHT_NO")
                }
                "3" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    HipheInfoLog(LauncherActivity.TAG, "Initiating MODE_NIGHT_FOLLOW_SYSTEM")
                }
            }
        } catch (e: Exception) {
            HipheErrorLog(LauncherActivity.TAG, "Error while Initiating Theme ", e.toString())
        }

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = Firebase.auth
        startService(Intent(this, UploaderService::class.java))

        networkStateCardView = findViewById(R.id.offlineNotifierCardMain)
        networkStateTextView = findViewById(R.id.offlineNotifierCardTextViewMain)
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isConnectedToNetwork ->
            notifyNetworkMode(
                isConnectedToNetwork,
                this,
                networkStateCardView,
                networkStateTextView
            )
        })

        val isVisitor = intent.getBooleanExtra(constants.isUserUnexplored, false)


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
            HipheInfoLog(TAG, "User is a newcomer, asking him if he wants to be guided")
            val b = MaterialAlertDialogBuilder(this)
            b.setIcon(R.drawable.ic_twotone_important_devices_24)
            b.setTitle(getString(R.string.visitor_welcome_to_hiphe))
            b.setMessage("Welcome\nIt is our pleasure to see you, here are some quick thing you can do :\n*View about Hiphe\n*Explore list of plants\n*Do your customisations in settings\n\nOr do you want to take a quik tour to our app? If yes, click \"OK\",else click \"NO, THANKS\"")
            b.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                HipheInfoLog(TAG, "User AGREED to take a tour")
                dialogInterface.dismiss()
                takeHipheTour()
                //TODO("GUIDE USER TO USE APPLICATION")
            }
            b.setNegativeButton(getString(R.string.no_thanks)) { dialogInterface, _ ->
                HipheInfoLog(TAG, "User DISAGREED to take a tour")
                dialogInterface.dismiss()
            }
            b.setCancelable(false)
            b.create()
            b.show()
        }
    }

    private fun takeHipheTour() {

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
                        "User is logging out without remembrance, googleSignInClient.revokeAccess() auth.signOut()"
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
                        "User is logging out with remembrance, googleSignInClient.revokeAccess() auth.signOut()"
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


        val speak =
            "$firstGuess is first guess with confidence level of ${firstGuesssConfidence}percent FROM ARTIFICIAL INTELLIGENCE "
        val accountInfo = getAccounts()
        if (accountInfo != null) {
            val acSpeak =
                "hey $accountInfo $firstGuess is first guess with confidence level of ${firstGuesssConfidence}percent FROM ARTIFICIAL INTELLIGENCE "
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