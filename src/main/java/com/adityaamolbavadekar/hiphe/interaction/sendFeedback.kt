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

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.adityaamolbavadekar.hiphe.*
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

fun sendFeedback(context: Context, thread: Thread, throwable: Throwable) {

    val mainData = userDeviceData(context)

    val endData = "\n\n ######## BEGIN Crash Real Data\n\n" +
            "Thread : $thread\n" +
            "Thread Name : ${thread.name}\n" +
            "Thread Id : ${thread.id}\n" +
            "Thread isAlive : ${thread.isAlive}\n" +
            "\n---------------------begin stacktrace\n ${thread.stackTrace}\n" +
            "\n---------------------end stacktrace\n\n" +
            "Throwable : $throwable\n" +
            "Throwable cause : ${throwable.cause}\n" +
            "Throwable message : ${throwable.message}\n" +
            "\n---------------------BEGIN ThrowableStacktrace \n ${throwable.stackTrace}\n" +
            "\n---------------------END ThrowableStacktrace\n" +
            "\n---------------------BEGIN Throwable stackTraceToString \n${throwable.stackTraceToString()}\n" +
            "\n---------------------END Throwable stackTraceToString \n" +
            "\n\n *Occurrence : ${
                SimpleDateFormat(
                        "EEE, dd mmm yyyy, HH:mm aaa Z",
                        Locale.ENGLISH
                ).format(Date())
            }\n\n" +
            "\n\n\n ######## END Crash Real Data\n"

    val fileName =
            "HipheCrashReport_" + SimpleDateFormat("dd_yyyy_HH_mm", Locale.ENGLISH).format(Date())
                    .toString() + ".txt"

    val value = "CRASH REPORT\n\n" + mainData + endData + "CRASH REPORT\n"
    val file = File(context.filesDir, fileName)
    file.writeText(value, Charset.defaultCharset())
    file.createNewFile()
    file.setReadable(true)
    file.setWritable(true)
    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    prefs.edit {
        putBoolean("CRASHED_PREVIOUSLY", true)
    }
    context.showLongToast("Crash File saved to " + file.toURI().toString() + " ")
    context.showLongToastWithGravity("The application crashed due to an unknown error. Please send this report to developers.")


    if (prefs.getBoolean("enable_crash_data_sending", true)) {
        createAchooser(context, value)
    } else {
        restartApplication(context)
    }


}

fun restartApplication(context: Context) {
    val TAG = "sendFeedback\$restartApplication"
    HipheWarningLog(TAG, "Triggering SystemExit(404) due to disabled share logging!!!")
    HipheWarningLog(TAG, "Triggering SystemExit(404) due to disabled share logging!!!")
    HipheWarningLog(TAG, "Triggering SystemExit(404) due to disabled share logging!!!")
    HipheWarningLog(TAG, "Triggering SystemExit(404) due to disabled share logging!!!")
    HipheWarningLog(TAG, "Triggering SystemExit(404) due to disabled share logging!!!")
    HipheWarningLog(TAG, "Triggering SystemExit(404) due to disabled share logging!!!")
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, LauncherActivity::class.java)
    intent.putExtra("LOGS", HipheLogger.toString())
    val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT)
    alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent)
    exitProcess(404)
}


fun showAlertOfCrash(context: Context, value: String) {
    val b = AlertDialog.Builder(context)
    b.setTitle("Hiphe Crashed!")
    b.setIcon(R.drawable.ic_baseline_bug_report_24)
    b.setMessage("The application crashed due to an unknown error.\nTo send this report to developers click \"SEND\" else click \"NO THANKS\"\n\n*Your concern is important to us.")
    b.setPositiveButton("SEND") { _, _ ->
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, value)
        }
        val shareIntent = Intent.createChooser(sendIntent, "Report a crash")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)
//        exitProcess(0)
    }
    b.setCancelable(true)
    b.setNegativeButton("NO THANKS") { _, _ ->
    }
    b.create()
    b.show()
}

fun createAchooser(context: Context, value: String) {

    Thread {
        val TAG = "sendFeedback\$createAchooser"
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, value)
            putExtra(Intent.EXTRA_TITLE, "Hiphe Crash")
        }
        val shareIntent = Intent.createChooser(sendIntent, "Report a crash")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, LauncherActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("LOGS", HipheLogger.toString())
        val pendingIntent =
                PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 7000, pendingIntent)
        exitProcess(404)
    }.start()

}

fun createAchooser2(context: Context, value: String) {

    Thread {
        val TAG = "sendFeedback\$createAchooser"
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")
        HipheWarningLog(TAG, "Triggering createAchooser due to a Crash!!!")

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, value)
            putExtra(Intent.EXTRA_TITLE, "Hiphe Crash")
        }
        val shareIntent = Intent.createChooser(sendIntent, "Report a crash")
        shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(shareIntent)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent2 = Intent(context, LauncherActivity::class.java)
        intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent2.putExtra("LOGS", HipheLogger.toString())
        val pendingIntent =
                PendingIntent.getActivity(context, 100, intent2, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 7000, pendingIntent)
        exitProcess(404)
    }.start()

}

