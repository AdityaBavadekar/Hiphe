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
import android.content.IntentSender
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.settings_activity.*
import java.io.File

class HipheSettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)


        try {
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            toolbar.setupWithNavController(navController)

        } catch (e: Exception) {
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference?.key) {
                "about" -> {
                    try {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                                .navigate(R.id.action_settingsFragmentMain_to_settingsFragmentAbout)
                    } catch (e: Exception) {
                        requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, AboutSettingsFragment()).commit()
                    }
                    return true
                }
                "Notifications" -> {
                    try {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                                .navigate(R.id.action_settingsFragmentMain_to_settingsFragmentNotifications)
                    } catch (e: Exception) {
                        requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment, NotificationsSettingsFragment())
                                .commit()
                    }
                    return true
                }
                "send_logs" -> {

                    try {
                        val path = File(requireActivity().filesDir, "FolderOne")
                        path.mkdirs()
                        val newTextFile = File(path, "SENT_CUSTOM_SETTINGS_LOGS.txt")
                        newTextFile.writeText("Sent Logs by User")
                        newTextFile.createNewFile()

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            val path2 = File(requireActivity().dataDir, "FolderOne")
                            path2.mkdirs()
                            val newTextFile2 = File(path2, "SENT_CUSTOM_SETTINGS_LOGS.txt")
                            newTextFile2.writeText("Sent Logs by User")
                            newTextFile2.createNewFile()
                        }
                    } catch (e: Exception) {
                    }
                    val cuser = Firebase.auth.currentUser
                    HipheInfoLog(TAG, "Action : SEND_LOGS initiated, getting USER_INFO")
                    HipheInfoLog(
                            TAG,
                            "Action : SEND_LOGS initiated, WITH USER AS : ${cuser?.email}"
                    )
                    HipheInfoLog(TAG, "Action : SEND_LOGS initiated, creating chooser")
                    val intent = Intent()
                    intent.action = Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TITLE, "Hiphe Logs")
                    intent.putExtra(Intent.EXTRA_TEXT, getTheFinalLogs().toString())
                    intent.type = "text/plain"
                    startActivity(Intent.createChooser(intent, "Send Logs"))

                    return true
                }
                else -> return false
            }
        }

    }

    class AboutSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences_about, rootKey)

        }
    }

    class NotificationsSettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences_notifications, rootKey)

        }
    }

    class YouAndHipheFragment : Fragment() {
        private lateinit var accountImageView: ImageView
        private lateinit var emailAddressTextView: TextView
        private lateinit var nameTextView: TextView

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.fragment_you_and_hiphe, container, false)
            val auth: FirebaseAuth = Firebase.auth
            accountImageView = root.findViewById(R.id.accountImageView)
            emailAddressTextView = root.findViewById(R.id.emailAddressTextView)
            nameTextView = root.findViewById(R.id.nameTextView)

            val user = auth.currentUser
            if (user != null) {
                val email = user.email
                val name = user.displayName
                val purl = user.photoUrl
                Glide.with(requireActivity())
                        .load(purl)
                        .into(accountImageView)
            }

            return root
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "HipheSettingsActivity"
    }

}