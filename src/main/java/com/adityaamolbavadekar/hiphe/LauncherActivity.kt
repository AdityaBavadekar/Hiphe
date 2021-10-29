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
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.interaction.*
import com.adityaamolbavadekar.hiphe.models.ChangeLogInfo
import com.adityaamolbavadekar.hiphe.models.GitRawApi
import com.adityaamolbavadekar.hiphe.network.ConnectionLiveData
import com.adityaamolbavadekar.hiphe.utils.ConfigureTheme
import com.adityaamolbavadekar.hiphe.utils.constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LauncherActivity : AppCompatActivity() {

    private val loggedOutUserTimeOut: Long = 1500
    private val normalTimeOut: Long = 1000
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this)
        if (intent.getStringExtra(getString(R.string.logs)) != null) {
            HipheDebugLog(TAG, intent.getStringExtra(getString(R.string.logs)).toString())
        }

        connectionLiveData.observe(this, Observer {
            if (!it) this.showToast(getString(R.string.you_are_offline))
        })


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val loggedIn = prefs.getBoolean(constants.checkIsLoggedInPrefKey, false)
        ConfigureTheme().configureThemeOnCreate(this, TAG)

        setContentView(R.layout.activity_launcher)
        if (loggedIn) {
            loggedInUserNormalBehaviour()
            HipheInfoLog(TAG, getString(R.string.user_is_logged_in_initiating_normal_call))
        } else {
            loggedOutUserClassicBehaviour()
            HipheInfoLog(TAG, getString(R.string.User_is_not_logged_in_initiating_classic_call))
        }


    }

    private fun loggedInUserNormalBehaviour() {
        //checkUpdates()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, normalTimeOut)
    }

    private fun checkUpdates() {
        try {

            val pckMangr = this.packageManager.getPackageInfo(
                this.packageName,
                0
            )
            val buildVersionName = pckMangr.versionName

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
                                this@LauncherActivity.showLongToast("You are on latest stable version of Hiphe i.e.$buildVersionName")
                                val b = MaterialAlertDialogBuilder(this@LauncherActivity)
                                b.setIcon(R.drawable.ic_baseline_system_update_24)
                                b.setTitle("Version $buildVersionName")
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
                                    this@LauncherActivity.showLongToast("Updating in background, you can continue exploring app")
                                    dialogInterface.dismiss()
                                    try {
                                        UpdateHipheWithUrl().updateHipheWithChangeInfo(
                                            this@LauncherActivity,
                                            changeLogInfo
                                        )
                                    } catch (e: Exception) {
                                    }
                                }
                                b.setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ ->
                                    dialogInterface.dismiss()
                                }
                                b.setCancelable(false)
                                b.create()
                                b.show()

                            } else if (buildVersionName == versionNameResult) {
//                                this@LauncherActivity.showLongToast("You are on latest stable version of Hiphe i.e.$buildVersionName")
//
//                                val b = MaterialAlertDialogBuilder(this@LauncherActivity)
//                                b.setIcon(R.drawable.ic_baseline_system_update_24)
//                                b.setTitle("Version $buildVersionName")
//                                b.setMessage(
//                                    getString(
//                                        R.string.your_are_on_latest_version_of_hiphe_formatted,
//                                        buildVersionName,
//                                        releaseNotesResult
//                                    )
//                                )
//                                b.setPositiveButton(getString(R.string.ok_got_it)) { dialogInterface, _ ->
//                                    dialogInterface.dismiss()
//                                }
//                                b.setCancelable(false)
//                                b.create()
//                                b.show()

                            }
                        }

                    } else {
                        HipheErrorLog(
                            HipheSettingsActivity.TAG,
                            "onResponse: Unsuccessful: ",
                            responseResult.code().toString()
                        )
                        this@LauncherActivity.showLongToast("Something went wrong...")
                        return
                    }

                }

                override fun onFailure(call: Call<List<ChangeLogInfo>>, t: Throwable) {
                    HipheErrorLog(
                        TAG,
                        "onFailure: LOCALIZED MESSAGE: ",
                        " ${t.localizedMessage}"
                    )
                    this@LauncherActivity.showLongToast("Please check that your Internet Connection is on")
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
            HipheErrorLog(TAG, "Error making call to $BASE_URL/changelog.json: ", e.toString())
        }

    }

    private fun loggedOutUserClassicBehaviour() {
        //checkUpdates()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }, loggedOutUserTimeOut)
    }

    companion object {
        const val TAG = "LauncherActivity"
        const val BASE_URL =
            "https://raw.githubusercontent.com/AdityaBavadekar/Hiphe/main/changelog/"
    }

}
