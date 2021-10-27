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

package com.adityaamolbavadekar.hiphe.interaction

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.utils.constants

class NetworkStateReceiver : BroadcastReceiver() {

//    private var OfflineAlertDialog: AlertDialog? = null

    override fun onReceive(context: Context?, intent: Intent?) {


        val builder = AlertDialog.Builder(context)
        builder.setTitle("Network Unavailable")
        builder.setIcon(R.drawable.ic_outline_link_off_24)
        builder.setMessage("The Network is unavailable at the moment, This could be due to Aeroplane mode, Wifi being off or Mobile Network being off. Please turn on Network.")
        builder.setPositiveButton("OPEN NETWORK SETTINGS") { dialog, lisp ->
            context?.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        builder.setCancelable(false)
        builder.create()
        val s = builder.create()
        if (intent != null && intent.action.equals(constants.networkStateKey)) {
            when (intent.getBooleanExtra(constants.networkStateKey, true)) {
                false -> {//User is Not Connected
                    s.show()
                }
                true -> {
                    if (s.isShowing) s.dismiss()
                }
            }
        }
    }
}
