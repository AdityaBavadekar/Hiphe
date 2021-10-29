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

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.interaction.*
import com.adityaamolbavadekar.hiphe.models.ChangeLogInfo
import com.adityaamolbavadekar.hiphe.models.GitRawApi
import com.adityaamolbavadekar.hiphe.ui.FaqsFragment
import com.adityaamolbavadekar.hiphe.ui.feedback.SendFeedbackFragment
import com.adityaamolbavadekar.hiphe.utils.constants
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.settings_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class HipheSettingsActivity : AppCompatActivity() {

    private lateinit var coordinatorLayout: CoordinatorLayout
    val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            dojsfgfdsi(isGranted)
        }

    fun dojsfgfdsi(isGranted: Boolean) {
        if (isGranted) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(this)

            HipheSettingsActivity.AboutSettingsFragment().download(
                Uri.parse(prefs.getString("update_uri", "").toString()),
                prefs.getString("fileName", "HipheUpdate").toString()
            )
        } else {
            showLongToast("Permission for write is required to update Hiphe, You can disable it afterwards from settingd")
        }
    }

    fun updateHipheWithChangeInfo(changeLogInfo: ChangeLogInfo) {
        val versionName = changeLogInfo.versionName
        val versionCode = changeLogInfo.versionCode
        val releaseNotes = changeLogInfo.releaseNotes
        val fileName = "Hiphe-$versionName"
        PreferenceManager.getDefaultSharedPreferences(this)
            .edit {
                putString("update_uri", changeLogInfo.apkURL)
                putString("fileName", fileName)
            }
        val apkURL = changeLogInfo.apkURL
        val apkUri = Uri.parse(apkURL)
        requestPermissionForWrite(apkUri, fileName)
    }


    fun requestPermissionForWrite(apkUri: Uri, fileName: String) {

        when (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )) {
            PackageManager.PERMISSION_DENIED -> {
                launchPermissionGrantRequest(apkUri, fileName)
            }
            PackageManager.PERMISSION_GRANTED -> {
                HipheSettingsActivity.AboutSettingsFragment().download(apkUri = apkUri, fileName)
            }
        }
    }


    fun launchPermissionGrantRequest(apkUri: Uri, fileName: String) {
        permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        when (prefs.getString("theme", "3")) {
            "1" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                HipheInfoLog(TAG, "Initiating MODE_NIGHT_YES")
            }
            "2" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                HipheInfoLog(TAG, "Initiating MODE_NIGHT_NO")
            }
            "3" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                HipheInfoLog(TAG, "Initiating MODE_NIGHT_FOLLOW_SYSTEM")
            }
        }
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)

        coordinatorLayout = findViewById(R.id.coordinator)

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
            findPreference<ListPreference>("theme")?.setOnPreferenceChangeListener { preference, newValue ->
                when (newValue.toString()) {
                    "1" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        HipheInfoLog(LauncherActivity.TAG, "Initiating MODE_NIGHT_YES")
                        true
                    }
                    "2" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        HipheInfoLog(LauncherActivity.TAG, "Initiating MODE_NIGHT_NO")
                        true
                    }
                    "3" -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        HipheInfoLog(LauncherActivity.TAG, "Initiating MODE_NIGHT_FOLLOW_SYSTEM")
                        true
                    }
                    else -> false
                }

            }

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
                "faqs" -> {
                    try {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.action_settingsFragmentMain_to_faqsFragment)
                    } catch (e: Exception) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, FaqsFragment())
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
            val pckMangr = requireActivity().packageManager.getPackageInfo(
                requireActivity().packageName,
                0
            )
            setPreferencesFromResource(R.xml.root_preferences_about, rootKey)
            findPreference<Preference>("build_version")?.summary = pckMangr.versionName
            findPreference<Preference>("build_version_code")?.summary =
                pckMangr.versionCode.toString()
            findPreference<Preference>("hiphe_check_update")?.summary =
                preferenceManager.sharedPreferences.getString(
                    constants.HIPHE_VERSION_UPDATE_KEY,
                    ""
                )
        }

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when (preference?.key) {
                "hiphe_check_update" -> {
                    val pckMangr = requireActivity().packageManager.getPackageInfo(
                        requireActivity().packageName,
                        0
                    )
                    val buildVersionName = pckMangr.versionName
                    try {
                        val retrofit = Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                        val gitRawApi: GitRawApi = retrofit.create(GitRawApi::class.java)

                        val call = gitRawApi.getNewUpdatesCall()


                        val callback = object : Callback<List<ChangeLogInfo>> {

                            override fun onResponse(
                                call: Call<List<ChangeLogInfo>>,
                                responseResult: Response<List<ChangeLogInfo>>
                            ) {

                                if (responseResult.isSuccessful) {

                                    val response = responseResult.body()?.get(0)
                                    val changeLogInfo: ChangeLogInfo? = response
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request was Successful"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET returned $changeLogInfo"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request Release Notes were ${changeLogInfo?.releaseNotes}"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request LatestVersion was ${changeLogInfo?.versionName}"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request LatestVersionCode was ${changeLogInfo?.versionCode}"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request apkURL was ${changeLogInfo?.apkURL}"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request's headers ${responseResult.headers()}"
                                    )
                                    HipheInfoLog(
                                        TAG,
                                        "@GET request's raw ${responseResult.raw()}"
                                    )

                                    if (changeLogInfo != null) {
                                        val versionNameResult = changeLogInfo.versionName
                                        val versionCodeResult = changeLogInfo.versionCode
                                        val releaseNotesResult = changeLogInfo.releaseNotes

                                        if (buildVersionName != versionNameResult) {

                                            val b = AlertDialog.Builder(requireActivity())
                                            b.setIcon(R.drawable.ic_baseline_system_update_24)
                                            b.setTitle("New Version $versionNameResult")
                                            b.setMessage(
                                                getString(
                                                    R.string.updated_version_of_hiphe_is_available_formatted,
                                                    versionNameResult,
                                                    buildVersionName,
                                                    versionNameResult,
                                                    releaseNotesResult
                                                )
                                            )
                                            b.setPositiveButton(getString(R.string.ok_update_hiphe)) { dialogInterface, _ ->
                                                requireActivity().showLongToast("Updating in background, you can continue exploring app")
                                                try {
                                                    HipheSettingsActivity().updateHipheWithChangeInfo(
                                                        changeLogInfo
                                                    )
                                                } catch (e: Exception) {
                                                    HipheErrorLog(
                                                        TAG,
                                                        "Error updating",
                                                        e.toString()
                                                    )
                                                }
                                                dialogInterface.dismiss()

                                            }
                                            b.setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                                                preferenceManager.sharedPreferences.edit {
                                                    putString(
                                                        constants.HIPHE_VERSION_UPDATE_KEY,
                                                        getString(
                                                            R.string.updated_version_of_hiphe_is_available_preference_formatted,
                                                            versionNameResult
                                                        )
                                                    )
                                                }
                                                preference.summary = getString(
                                                    R.string.updated_version_of_hiphe_is_available_preference_formatted,
                                                    versionNameResult
                                                )
                                                dialogInterface.dismiss()
                                            }
                                            b.setCancelable(false)
                                            b.create()
                                            b.show()

                                        } else if (buildVersionName == versionNameResult) {
                                            requireActivity().showLongToast("You are on latest stable version of Hiphe i.e.$buildVersionName")

                                            val b = AlertDialog.Builder(requireActivity())
                                            b.setIcon(R.drawable.ic_baseline_system_update_24)
                                            b.setTitle("Version $buildVersionName")
                                            b.setMessage(
                                                getString(
                                                    R.string.your_are_on_latest_version_of_hiphe_formatted,
                                                    buildVersionName,
                                                    releaseNotesResult
                                                )
                                            )
                                            b.setPositiveButton(getString(R.string.ok_got_it)) { dialogInterface, _ ->
                                                dialogInterface.dismiss()
                                            }
                                            b.setCancelable(false)
                                            b.create()
                                            b.show()

                                        }
                                    }

                                } else {
                                    HipheErrorLog(
                                        TAG,
                                        "onResponse: Unsuccessful: ",
                                        responseResult.code().toString()
                                    )
                                    requireActivity().showLongToast("Something went wrong...")
                                    return
                                }

                            }

                            override fun onFailure(call: Call<List<ChangeLogInfo>>, t: Throwable) {
                                HipheErrorLog(
                                    TAG,
                                    "onFailure: LOCALIZED MESSAGE: ",
                                    " ${t.localizedMessage}"
                                )
                                requireActivity().showLongToast("Please check that your Internet Connection is on")
                                HipheErrorLog(
                                    TAG,
                                    "onFailure: STACKTRACE: ",
                                    t.stackTraceToString()
                                )
                                HipheErrorLog(
                                    TAG,
                                    "onFailure: DATA: ",
                                    t.cause.toString()
                                )
                            }

                        }

                        call.enqueue(callback)
                    } catch (e: Exception) {
                        HipheErrorLog(
                            LauncherActivity.TAG,
                            getString(R.string.error_making_call_to_changelog_json_url, BASE_URL),
                            e.toString()
                        )
                    }

                    return true
                }
                "hpp" -> {
                    try {
                        val client = CustomTabsClient.bindCustomTabsService(
                            requireContext(),
                            constants.packageName,
                            object :
                                CustomTabsServiceConnection() {
                                override fun onServiceDisconnected(name: ComponentName?) {
                                }

                                override fun onCustomTabsServiceConnected(
                                    name: ComponentName,
                                    client: CustomTabsClient
                                ) {
                                    client.warmup(1000)
                                }

                            })
                    } catch (e: Exception) {
                    }
                    val url =
                        getString(R.string.hiphe_github_privacy_policy_url)
                    val builder = CustomTabsIntent.Builder()
                    val colorInt: Int = Color.parseColor("#FF0000") //red
                    val defaultColors = CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(colorInt)
                        .build()
                    builder.setDefaultColorSchemeParams(defaultColors)
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))
                    return true
                }
                "hts" -> {
                    val url =
                        getString(R.string.hiphe_github_terms_and_conditions_url)
                    val builder = CustomTabsIntent.Builder()
                    val colorInt: Int = Color.parseColor("#FF0000") //red
                    val defaultColors = CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(colorInt)
                        .build()

                    builder.setDefaultColorSchemeParams(defaultColors)
                    val customTabsIntent = builder.build()
                    customTabsIntent.launchUrl(requireActivity(), Uri.parse(url))
                    return true
                }
                "send_feedback" -> {
                    val firestore = Firebase.firestore
                    firestore.collection("HIPHE_REMOTE_USER_SENT_FEEDBACKS")
                        .document("TEST_${System.currentTimeMillis()}")
                        .set(hashMapOf("FEEDBACK" to true))
                        .addOnSuccessListener {
                            requireActivity().showToast("Feedback sent")
                        }
                        .addOnFailureListener {
                            requireActivity().showToast("Feedback not sent, ${it.cause}")
                        }
//                    try {
//                        ScreenShot.bitmap = CaptureScreenShot(requireActivity().findViewById(R.id.coordinator)).capture()
//                    } catch (e: Exception) {
//                    }
                    startActivity(Intent(requireActivity(), SendFeedbackFragment::class.java))

                    return true
                }
                else -> return false
            }
        }


        fun download(apkUri: Uri, fileName: String) {
            //Download APK
            val downloadManager =
                requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(apkUri)
            request.setTitle(getString(R.string.updating_hiphe))
            request.setDescription(getString(R.string.we_will_download_apk_in_background))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            request.allowScanningByMediaScanner()

            val completion = downloadManager.enqueue(request)

            //requestPermissionForWrite()
            requireActivity().showLongToast(
                downloadManager.getUriForDownloadedFile(completion).toString()
            )
            try {
                downloadManager.openDownloadedFile(completion)
                requireActivity().showToast("After downloading click Install")
            } catch (e: Exception) {
                HipheErrorLog(TAG, getString(R.string.error_opening_downloaded_file), e.toString())
            }
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
        const val BASE_URL =
            "https://raw.githubusercontent.com/AdityaBavadekar/Hiphe/main/changelog/"
    }

}