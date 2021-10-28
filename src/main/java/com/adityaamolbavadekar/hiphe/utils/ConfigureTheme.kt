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

package com.adityaamolbavadekar.hiphe.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog

class ConfigureTheme {

    fun configureThemeOnCreate(context: Context, TAG: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        try {
            when (prefs.getString("theme", "3")) {
                "1" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    HipheInfoLog(TAG, context.getString(R.string.Initiating_MODE_NIGHT_YES))
                }
                "2" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    HipheInfoLog(TAG, context.getString(R.string.Initiating_MODE_NIGHT_NO))
                }
                "3" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    HipheInfoLog(
                        TAG,
                        context.getString(R.string.Initiating_MODE_NIGHT_FOLLOW_SYSTEM)
                    )
                }
            }
        } catch (e: Exception) {
            HipheErrorLog(
                TAG,
                context.getString(R.string.error_while_initiating_theme),
                e.toString()
            )
        }
    }
}