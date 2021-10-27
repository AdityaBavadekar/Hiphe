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
import com.adityaamolbavadekar.hiphe.interaction.HipheDebugLog
import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.adityaamolbavadekar.hiphe.models.ChangeLogInfo
import com.adityaamolbavadekar.hiphe.models.GitRawApi
import com.adityaamolbavadekar.hiphe.network.ConnectionLiveData
import com.adityaamolbavadekar.hiphe.ui.home.HomeFragment
import com.adityaamolbavadekar.hiphe.utils.ConfigureTheme
import com.adityaamolbavadekar.hiphe.utils.constants
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
        if (intent.getStringExtra("LOGS") != null) {
            HipheDebugLog(TAG, intent.getStringExtra("LOGS").toString())
        }

        connectionLiveData.observe(this, Observer {
            if (!it) this.showToast("You are offline!")
        })

        try {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val gitRawApi: GitRawApi = retrofit.create(GitRawApi::class.java)

            val call = gitRawApi.getNewUpdatesCall()
            val callback = object : Callback<ChangeLogInfo> {
                override fun onResponse(
                    call: Call<ChangeLogInfo>,
                    response: Response<ChangeLogInfo>
                ) {
                    if (response.isSuccessful) {
                        val changeLogInfo: ChangeLogInfo? = response.body()
                        HipheInfoLog(TAG, "@GET request was Successful")
                        HipheInfoLog(TAG, "@GET returned $changeLogInfo")
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
                        HipheInfoLog(TAG, "@GET request's headers ${response.headers()}")
                        HipheInfoLog(TAG, "@GET request's raw ${response.raw()}")


                    } else {
                        HipheErrorLog(
                            HomeFragment.TAG,
                            "onResponse: Unsuccessful: ",
                            response.code().toString()
                        )
                        return
                    }
                }

                override fun onFailure(call: Call<ChangeLogInfo>, t: Throwable) {
                    HipheErrorLog(
                        HomeFragment.TAG,
                        "onFailure: LOCALIZED MESSAGE: ",
                        " ${t.localizedMessage}"
                    )
                    HipheErrorLog(
                        HomeFragment.TAG,
                        "onFailure: STACKTRACE: ",
                        t.stackTraceToString()
                    )
                    HipheErrorLog(HomeFragment.TAG, "onFailure: DATA: ", t.cause.toString())
                }
            }
            call.enqueue(callback)
        } catch (e: Exception) {
            HipheErrorLog(TAG, "Error making call to $BASE_URL/changelog.json: ", e.toString())
        }


        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val loggedIn = prefs.getBoolean(constants.checkIsLoggedInPrefKey, false)
        ConfigureTheme().configureThemeOnCreate(this)

        setContentView(R.layout.activity_launcher)
        if (loggedIn) {
            loggedInUserNormalBehaviour()
            HipheInfoLog(TAG, "User is logged in, initiating normal call")
        } else {
            loggedOutUserClassicBehaviour()
            HipheInfoLog(TAG, "User is not logged in, initiating classic call")
        }


    }

    private fun loggedInUserNormalBehaviour() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, normalTimeOut)
    }

    private fun loggedOutUserClassicBehaviour() {
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
