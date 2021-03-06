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

import java.text.SimpleDateFormat
import java.util.*

val HipheLogger = mutableListOf<HipheLog>(
    HipheLog(
        "HipheLogger",
        "----------------------------------START----------------------------------",
        HipheLog.LogType.Warning
    ),
    HipheLog(
        "HipheLogger",
        "----------------------------------START----------------------------------",
        HipheLog.LogType.Warning
    ),
    HipheLog(
        "HipheLogger",
        "----------------------------------START----------------------------------",
        HipheLog.LogType.Warning
    ),
    HipheLog("HipheLogger", "Default Info Log by HipheLogger", HipheLog.LogType.Info),
    HipheLog("HipheLogger", "Default Warning Log by HipheLogger", HipheLog.LogType.Warning),
    HipheLog("HipheLogger", "Default Error Log by HipheLogger", HipheLog.LogType.Error),
    HipheLog("HipheLogger", "Default Verbose Log by HipheLogger", HipheLog.LogType.Verbose)
)


data class HipheLog(
    val TAG: String,
    val LogBody: String,
    val LogTypeInfo: LogType
) {
    enum class LogType {
        Info, Warning, Error, Verbose, Debug, UserInfo, Timestamp
    }
}

fun HipheInfoLog(TAG: String, LogBody: String) {
    val timestamp =
        SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString()
    HipheLogger.add(HipheLog(TAG, "ON $timestamp >>> $LogBody", HipheLog.LogType.Info))
}

fun HipheDebugLog(TAG: String, LogBody: String) {
    val timestamp =
        SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString()

    HipheLogger.add(HipheLog(TAG, "ON $timestamp >>> $LogBody", HipheLog.LogType.Debug))
}

fun HipheUserInfoInfoLog(TAG: String, LogBody: String) {
    val timestamp =
        SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString()

    HipheLogger.add(HipheLog(TAG, "ON $timestamp >>> $LogBody", HipheLog.LogType.UserInfo))

}

fun HipheWarningLog(TAG: String, LogBody: String) {
    val timestamp =
        SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString()

    HipheLogger.add(HipheLog(TAG, "ON $timestamp >>> $LogBody", HipheLog.LogType.Warning))
}

fun HipheErrorLog(TAG: String, LogBody: String, Error: String) {
    val timestamp =
        SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString()

    HipheLogger.add(
        HipheLog(
            TAG,
            "ON $timestamp >>> $LogBody    ERROR!! : $Error",
            HipheLog.LogType.Error
        )
    )
}

fun HipheVerboseLog(TAG: String, LogBody: String) {
    val timestamp =
        SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString()
    HipheLogger.add(HipheLog(TAG, "ON $timestamp >>> $LogBody", HipheLog.LogType.Verbose))
}

fun getTheFinalLogs(): MutableList<HipheLog> {

    HipheLogger.add(
        HipheLog(
            "HipheLogger",
            "----------------------------------START-TIMESTAMP----------------------------------",
            HipheLog.LogType.Timestamp
        )
    )
    HipheLogger.add(
        HipheLog(
            "HipheLogger",
            SimpleDateFormat("E, dd m yyyy HH:mm aaa Z", Locale.ENGLISH).format(Date()).toString(),
            HipheLog.LogType.Timestamp
        )
    )
    HipheLogger.add(
        HipheLog(
            "HipheLogger",
            "----------------------------------END-TIMESTAMP----------------------------------",
            HipheLog.LogType.Timestamp
        )
    )

    HipheWarningLog(
        "HipheLogger",
        "----------------------------------END----------------------------------"
    )
    HipheWarningLog(
        "HipheLogger",
        "----------------------------------END----------------------------------"
    )
    HipheWarningLog(
        "HipheLogger",
        "----------------------------------END----------------------------------"
    )
    HipheWarningLog(
        "HipheLogger",
        "----------------------------------END----------------------------------"
    )
    return HipheLogger
}