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

import android.content.IntentFilter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.adityaamolbavadekar.hiphe.interaction.CreateChannels
import com.adityaamolbavadekar.hiphe.interaction.NetworkStateReceiver
import com.adityaamolbavadekar.hiphe.interaction.NotifyNetworkInfo
import com.adityaamolbavadekar.hiphe.network.ConnectionLiveData
import com.adityaamolbavadekar.hiphe.ui.googlesign.GoogleSignInFragment
import com.adityaamolbavadekar.hiphe.utils.ConfigureTheme
import com.adityaamolbavadekar.hiphe.utils.constants
import kotlinx.android.synthetic.main.settings_activity.*

class IntroActivity : AppCompatActivity() {


    companion object {
        const val TAG = "IntroActivity"
    }

    private lateinit var networkStateCardView: CardView
    private lateinit var networkStateTextView: TextView
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ConfigureTheme().configureThemeOnCreate(this, TAG)
        setContentView(R.layout.activity_intro)

        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        try {
            toolbar.setupWithNavController(navController)
        } catch (e: Exception) {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, GoogleSignInFragment())
                    .commit()
            }
        }

        val receiver = NetworkStateReceiver()
        registerReceiver(receiver, IntentFilter(constants.networkStateKey))
        connectionLiveData = ConnectionLiveData(this)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CreateChannels.Builder(this)
                .createChannels(
                listOf(
                    CreateChannels.Channel(
                        "Miscellaneous",
                        "",
                        CreateChannels.Channel.Importance.Medium,
                        null
                    )
                )
            )
        }

        networkStateCardView = findViewById(R.id.offlineNotifierCardIntro)
        networkStateTextView = findViewById(R.id.offlineNotifierCardTextViewIntro)
        connectionLiveData.observeForever { isConnectedToNetwork ->
            NotifyNetworkInfo().notifyNetworkMode(
                isConnectedToNetwork,
                this,
                networkStateCardView,
                networkStateTextView
            )
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}