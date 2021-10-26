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
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.interaction.NetworkStateReceiver
import com.adityaamolbavadekar.hiphe.services.InternetStateCheckerService
import com.adityaamolbavadekar.hiphe.utils.constants

class LauncherActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST_CODE: Int = 404
    private val loggedOutUserTimeOut: Long = 1500
    private val normalTimeOut: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, InternetStateCheckerService::class.java))
        val receiver = NetworkStateReceiver()
        registerReceiver(receiver, IntentFilter(constants.networkStateKey))
        if (intent.getStringExtra("LOGS") != null) {
            HipheDebugLog(TAG, intent.getStringExtra("LOGS").toString())
        }
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val loggedIn = prefs.getBoolean(constants.checkIsLoggedInPrefKey, false)
        try {
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
        } catch (e: Exception) {
            HipheErrorLog(TAG, "Error while Initiating Theme ", e.toString())
        }

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
    }

}