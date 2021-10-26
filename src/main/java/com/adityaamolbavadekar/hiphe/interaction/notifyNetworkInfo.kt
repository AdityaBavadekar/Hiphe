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

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.adityaamolbavadekar.hiphe.R

fun notifyNetworkMode(
    networkStateOnline: Boolean,
    context: Context,
    networkStateNotifierCardView: CardView,
    networkStateNotifierTextView: TextView
) {
    when (networkStateOnline) {
        true -> notifyOnlineMode(
            networkStateNotifierCardView,
            networkStateNotifierTextView,
            context
        )
        false -> notifyOfflineMode(
            networkStateNotifierCardView,
            networkStateNotifierTextView,
            context
        )
    }
}

fun notifyOnlineMode(
    networkStateNotifierCardView: CardView,
    networkStateNotifierTextView: TextView,
    context: Context
) {
    networkStateNotifierTextView.text = context.getString(R.string.title_back_online)
    Handler(Looper.getMainLooper()).postDelayed({
        if (networkStateNotifierCardView.isVisible) networkStateNotifierCardView.visibility =
            View.GONE//hide Card
    }, 1000)
}

fun notifyOfflineMode(
    networkStateNotifierCardView: CardView,
    networkStateNotifierTextView: TextView,
    context: Context
) {
    networkStateNotifierTextView.text = context.getString(R.string.title_offline_no_connection)
    if (!networkStateNotifierCardView.isVisible) networkStateNotifierCardView.visibility =
        View.VISIBLE//show Card
}