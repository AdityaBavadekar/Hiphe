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
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class SendFeedback() {

    fun sendFeedback(context: Context, thread: Thread, throwable: Throwable) {
        CoroutineScope(Dispatchers.IO).launch {
            val mainData = UserDeviceData().userDeviceData(context)
            val endData = getEndData(thread, throwable)
            val value = "CRASH REPORT\n\n" + mainData + endData + "CRASH REPORT\n"
            val fileName = writeToFile(context, value)

            val user = Firebase.auth.currentUser
            if (user != null) {
                SendDataToFirestore(context, value, fileName, mainData, user.email).start()
            } else {
                SendDataToFirestore(
                    context,
                    value,
                    fileName,
                    mainData,
                    "@@USER_WAS_NOT_SIGNED_UP@@"
                ).start()
            }
        }.start()
    }

    private fun getEndData(thread: Thread, throwable: Throwable): String {

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
                "\n---------------------BEGIN Throwable StackTraceString \n${throwable.stackTraceToString()}\n" +
                "\n---------------------END Throwable StackTraceString \n" +
                "\n\n *Occurrence : ${
                    SimpleDateFormat(
                        "EEE, dd mmm yyyy, HH:mm aaa Z",
                        Locale.ENGLISH
                    ).format(Date())
                }\n\n" +
                "\n\n\n ######## END Crash Real Data\n"

        return endData

    }

    private fun writeToFile(context: Context, value: String): String {
        val fileName =
            "HipheCrashReport_" + SimpleDateFormat("dd_yyyy_HH_mm", Locale.ENGLISH).format(Date())
                .toString() + ".txt"

        val file = File(context.filesDir, fileName)
        file.writeText(value, Charset.defaultCharset())
        file.createNewFile()
        file.setReadable(true)
        file.setWritable(true)
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit {
            putBoolean("CRASHED_PREVIOUSLY", true)
        }
        context.fileList()
        context.showLongToast("Crash File saved to " + file.toURI().toString() + " ")
        context.showLongToastWithGravity("Sorry, the application crashed")
        return fileName
    }


//
//    private fun sendCrashData(
//        context: Context,
//        value: String,
//        mainData: String,
//        fileName: String,
//        email: String?
//    ) {
//        context.showLongToastWithGravity("The crash data is being sent")
//        val TAG = "sendFeedback\$sendCrashData"
//        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
//        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
//        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
//        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
//        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
//        HipheWarningLog(TAG, "Sending crash data to Firestore and restarting application!!!")
//        val dataToSend = hashMapOf(
//            "USER_EMAIL_ID" to "$email",
//            "CRASH_DEVICE_DATA" to "$mainData",
//            "CRASH_MAIN_DATA" to "$value",
//            "CRASH_PATH_TO_SAVED_DATA" to "${context.filesDir.path}",
//            "CRASH_FILES_IN_PATH_PARENT" to "${context.fileList()}",
//            "CRASH_HIPHE_LOGS" to "${getTheFinalLogs()}",
//            "LOCALE_COUNTRY" to "${Locale.getDefault().country}",
//            "LOCALE_DISPLAY_COUNTRY" to "${Locale.getDefault().displayCountry}",
//            "LOCALE_IS03_DISPLAY_COUNTRY" to "${Locale.getDefault().isO3Country}",
//            "LOCALE_ISO3_DISPLAY_LANGUAGE" to "${Locale.getDefault().isO3Language}"
//        )
//
//        val firestore = Firebase.firestore
//        firestore.collection("HIPHE_REMOTE_CRASHES")
//            .document("$fileName")
//            .set(dataToSend)
//            .addOnSuccessListener {
////                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////                val intent = Intent(context, LauncherActivity::class.java)
////                intent.putExtra("LOGS", HipheLogger.toString())
////                val pendingIntent =
////                    PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT)
////                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent)
//
//                val intent = Intent(context, LauncherActivity::class.java)
//                intent.putExtra("LOGS", HipheLogger.toString())
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(intent)
//                exitProcess(404)
//            }
//            .addOnFailureListener {
//                val intent = Intent(context, LauncherActivity::class.java)
//                intent.putExtra("LOGS", HipheLogger.toString())
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                context.startActivity(intent)
//                exitProcess(404)
//            }
//
//    }
//
//    private fun restartApplication(context: Context) {
//        val TAG = "sendFeedback\$restartApplication"
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
//        HipheWarningLog(
//            TAG,
//            context.getString(R.string.Triggering_Crash_Upload_and_then_SystemExit_due_to_disabled_share_crash_data_in_settings)
//        )
////
////        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        val intent = Intent(context, LauncherActivity::class.java)
////        val pendingIntent =
////            PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT)
////        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, pendingIntent)
//        val intent = Intent(context, LauncherActivity::class.java)
//        intent.putExtra("LOGS", HipheLogger.toString())
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(intent)
//        exitProcess(404)
//    }
//
//    private fun showAlertOfCrash(context: Context, value: String) {
//        val b = AlertDialog.Builder(context)
//        b.setTitle("Hiphe Crashed!")
//        b.setIcon(R.drawable.ic_baseline_bug_report_24)
//        b.setMessage("The application crashed due to an unknown error.\nTo send this report to developers click \"SEND\" else click \"NO THANKS\"\n\n*Your concern is important to us.")
//        b.setPositiveButton("SEND") { _, _ ->
//            val sendIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                type = "text/plain"
//                putExtra(Intent.EXTRA_TEXT, value)
//            }
//            val shareIntent = Intent.createChooser(sendIntent, "Report a crash")
//            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(shareIntent)
////        exitProcess(0)
//        }
//        b.setCancelable(true)
//        b.setNegativeButton("NO THANKS") { _, _ ->
//        }
//        b.create()
//        b.show()
//    }
//
//    private fun createChooser(context: Context, value: String) {
//        context.showLongToastWithGravity(context.getString(R.string.please_send_this_crash_data_to_developer))
//
//        Thread {
//            val TAG = "sendFeedback\$createAchooser"
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//            HipheWarningLog(
//                TAG,
//                context.getString(R.string.Triggering_createAchooser_due_to_a_Crash)
//            )
//
//            val sendIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                type = "text/plain"
//                putExtra(Intent.EXTRA_TEXT, value)
//                putExtra(Intent.EXTRA_TITLE, context.getString(R.string.hiphe_crash))
//            }
//            val shareIntent =
//                Intent.createChooser(sendIntent, context.getString(R.string.report_a_crash))
//            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(shareIntent)
////
////            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
////            val intent = Intent(context, LauncherActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
////            intent.putExtra("LOGS", HipheLogger.toString())
////            val pendingIntent =
////                PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT)
////            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 7000, pendingIntent)
//            exitProcess(404)
//        }.start()
//
//    }
}
