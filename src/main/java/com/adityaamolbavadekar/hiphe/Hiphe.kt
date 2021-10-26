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

import android.app.Application
import android.content.Context
import com.adityaamolbavadekar.hiphe.interaction.sendFeedback
import com.adityaamolbavadekar.hiphe.interaction.showLongToast

/*
class Hiphe : Application() {
    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) return
        val handler = Thread.getDefaultUncaughtExceptionHandler()
        try {
            Thread.setDefaultUncaughtExceptionHandler { t, e ->

                try {
                    sendFeedbackOfErrorHiphe(this@Hiphe, t, e)
                    //                val stringWriter = StringWriter()
                    //                e.printStackTrace(PrintWriter(stringWriter))
                    //                val intent = Intent(Intent.ACTION_SEND)
                    //                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    //                intent.putExtra(Intent.EXTRA_TEXT, stringWriter.toString())
                    //                startActivity(intent)

                } catch (e: Exception) {
                } finally {
                    if (handler != null) {
                        handler.uncaughtException(t, e)
                    } else {

                    }
                }

            }
        } catch (e: Exception) {
        }
    }

    private val uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { t, e ->
        sendFeedbackOfErrorHiphe(this@Hiphe, t, e)
    }

    private fun sendFeedbackOfErrorHiphe(context: Context, thread: Thread, throwable: Throwable) {
        val applicationErrorReport = ApplicationErrorReport()
        applicationErrorReport.packageName = Application().packageName
        applicationErrorReport.processName = Application().packageName
        applicationErrorReport.time = System.currentTimeMillis()
        applicationErrorReport.type = ApplicationErrorReport.TYPE_CRASH
        applicationErrorReport.systemApp = false
        val crash = ApplicationErrorReport.CrashInfo()
        crash.exceptionClassName = throwable.javaClass.toString()
        crash.exceptionMessage = throwable.toString()
        crash.stackTrace = throwable.stackTrace.toString()
        applicationErrorReport.crashInfo = crash
        val intent = Intent(Intent.ACTION_APP_ERROR)
        intent.putExtra(Intent.EXTRA_BUG_REPORT, applicationErrorReport)
        context.startActivity(intent)

    }
}
*/
class Hiphe : Application() {

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
                sendFeedback(context, t, e)
            } catch (e: Exception) {
                context.showLongToast(e.toString())
            }
        }
    }

//        Thread.getDefaultUncaughtExceptionHandler()
}


/*
    class CustomExceptionHandler : Thread.UncaughtExceptionHandler {
        private lateinit var uncaughtExceptionHandler: Thread.UncaughtExceptionHandler

        fun CustomExceptionHandler(uncaughtExceptionHandler_: Thread.UncaughtExceptionHandler) {
            uncaughtExceptionHandler = uncaughtExceptionHandler_
        }

        override fun uncaughtException(t: Thread, e: Throwable) {
            val model = Build.MODEL
            val manufacturer = Build.MANUFACTURER
            val id = Build.ID
            val device = Build.DEVICE
            val brand = Build.BRAND
            val androidOs = Build.VERSION.SDK_INT
            val product = Build.PRODUCT
            val packageName = PackageInfo().packageName
            val versionName = PackageInfo().versionName
            val installLoation = PackageInfo().installLocation
            val permissionsGranted = PackageInfo().permissions
            val longVersionCode = PackageInfo().versionCode
            val androidVersion = Build.VERSION.RELEASE
            val time = Build.TIME
            val user = Build.USER
            val freeSpace = Environment.getDataDirectory().freeSpace
            val totalSpace = Environment.getDataDirectory().totalSpace
            val data =
                "ID :" + id + " DEVICE :" + device + "BRAND :" + brand + " PRODUCT :" + product + " Android Version :" + androidOs
        }

    }*/

/*
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initAcra {
            buildConfigClass = BuildConfig::class.java
            reportFormat = StringFormat.JSON
            toast {
                text = getString(R.string.app_name)
            }
            mailSender {
                mailTo = "adityarbavadekar@gmail.com"
                val dated = SimpleDateFormat(
                    "EEE, dd MMM yyyy\' at \'HH:mm aaa Z",
                    Locale.ENGLISH
                ).format(Date()).toString()
                reportAsFile = true
                reportFileName = "HipheCrash_On_${dated}_Report.txt"
                body = "Hiphe Crashed \n\n\n Occurrence : $dated"
            }
        }
    }
*/
//}
