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

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.adityaamolbavadekar.hiphe.interaction.NetworkStateReceiver
import com.adityaamolbavadekar.hiphe.interaction.notifyNetworkMode
import com.adityaamolbavadekar.hiphe.ui.googlesign.GoogleSignInFragment
import com.adityaamolbavadekar.hiphe.utils.constants

class IntroActivity : AppCompatActivity() {


    private val TAG = "IntroActivity"
    private lateinit var networkStateCardView: CardView
    private lateinit var networkStateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val receiver = NetworkStateReceiver()
        registerReceiver(receiver, IntentFilter(constants.networkStateKey))
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_intro, GoogleSignInFragment())
                    .commit()
        }
        Log.i(TAG, "Custom INFO LOG1 IntroActivity\$onCreate")
        Log.i(TAG, "Custom INFO LOG2 IntroActivity\$onCreate")
        Log.i(TAG, "Custom INFO LOG3 IntroActivity\$onCreate")
        Log.i(TAG, "Custom INFO LOG4 IntroActivity\$onCreate")
        Log.d(TAG, "Custom DEBUG LOG1 IntroActivity\$onCreate")
        Log.d(TAG, "Custom DEBUG LOG2 IntroActivity\$onCreate")
        Log.d(TAG, "Custom DEBUG LOG3 IntroActivity\$onCreate")
        Log.d(TAG, "Custom DEBUG LOG4 IntroActivity\$onCreate")


        networkStateCardView = findViewById(R.id.offlineNotifierCardIntro)
        networkStateTextView = findViewById(R.id.offlineNotifierCardTextViewIntro)
        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isConnectedToNetwork ->
            notifyNetworkMode(
                    isConnectedToNetwork,
                    this,
                    networkStateCardView,
                    networkStateTextView
            )
        })
    }
}