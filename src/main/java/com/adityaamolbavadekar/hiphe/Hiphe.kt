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

import android.app.Application
import android.content.Context
import com.adityaamolbavadekar.hiphe.interaction.SendFeedback
import com.adityaamolbavadekar.hiphe.interaction.showLongToast
import com.adityaamolbavadekar.hiphe.room.note.NoteDatabaseClass

class Hiphe : Application() {

    val notesDatabase: NoteDatabaseClass by lazy {
        NoteDatabaseClass.getDatabase(this)
    }

    init {
        instance = this
    }

    companion object {
        private var instance: Hiphe? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context =
            applicationContext()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            try {
                SendFeedback().sendFeedback(context, t, e)
            } catch (e: Exception) {
                context.showLongToast(e.toString())
            }
        }
    }
}