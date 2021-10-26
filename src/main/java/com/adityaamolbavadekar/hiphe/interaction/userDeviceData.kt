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

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Environment
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

fun userDeviceData(context: Context): String {

    val model = Build.MODEL
    val manufacturer = Build.MANUFACTURER
    val id = Build.ID
    val device = Build.DEVICE
    val brand = Build.BRAND
    val androidOs = Build.VERSION.SDK_INT
    val product = Build.PRODUCT
    val pckMangr = context.packageManager.getPackageInfo(context.packageName, 0)
    val packageName = pckMangr.packageName
    val versionName = pckMangr.versionName
    val requestedPermissions = pckMangr.requestedPermissions
    val activities = pckMangr.activities
//    val listOfAppsRunning = getRunningApps(context)
//    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    val networkOperatorName = tm.networkOperatorName
    val installLoacation = pckMangr.installLocation
    val permissionsGranted = PackageInfo().permissions
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pckMangr.longVersionCode
    } else {
        pckMangr.versionCode
    }
    val androidVersion = Build.VERSION.RELEASE
    val time = Build.TIME
    val user = Build.USER
    val freeSpace = Environment.getDataDirectory().freeSpace
    val totalSpace = Environment.getDataDirectory().totalSpace
    val timeZone = TimeZone.getDefault().id
    val locale = Locale.getDefault()

    val unknown = Build.UNKNOWN
    val type = Build.TYPE
    val fingerprint = Build.FINGERPRINT
    val hardware = Build.HARDWARE
    val host = Build.HOST
    val display = Build.DISPLAY
    val tags = Build.TAGS
    val sw = StringWriter()
    val pw = PrintWriter(sw)
    val stackTrace = sw.toString()
    pw.close()

    val data =
        "\n*HIPHE CRASH REPORT*\n\n\n\nTIMEZONE : $timeZone\nLOCALE(DEFAULT) : $locale\nUNKNOWN : $unknown\nTYPE : $type\nFingerprint : $fingerprint\nHardware : $hardware\nHost : $host\nDisplay : $display\nTAGS : $tags\nMODEL : $model\nMANUFACTURER : $manufacturer\nUSER : $user\nID : $id\nDEVICE : $device\nBRAND : $brand\nPRODUCT : $product \nNetwork Operator : networkOperatorName\nAndroid SDK Version : $androidOs\nAndroid Release Version : $androidVersion\nFree Space: $freeSpace\nTotal Space : $totalSpace\n\n\nPackage Name : $packageName\nVersion Name : $versionName\nVersion Code : $versionCode\nBuild Time : $time\nBuild Time : $time\nInstall Location : $installLoacation\nPermissions: $permissionsGranted\nRequested Permissions: $requestedPermissions\nActivities: $activities\nApps Running: listOfAppsRunning\n\nHIPHE LOGS : $HipheLogger\n\n\nSTART STACKTRACE >> \n$stackTrace\n\n\n>>END STACKTRACE\n\n\n"

    return data
}

fun userDeviceDataHashmap(context: Context): HashMap<String, String> {

    val model = Build.MODEL
    val manufacturer = Build.MANUFACTURER
    val id = Build.ID
    val device = Build.DEVICE
    val brand = Build.BRAND
    val androidOs = Build.VERSION.SDK_INT
    val product = Build.PRODUCT
    val pckMangr = context.packageManager.getPackageInfo(context.packageName, 0)
    val packageName = pckMangr.packageName
    val versionName = pckMangr.versionName
    val requestedPermissions = pckMangr.requestedPermissions
    val activities = pckMangr.activities
//    val listOfAppsRunning = getRunningApps(context)
//    val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//    val networkOperatorName = tm.networkOperatorName
    val installLoacation = pckMangr.installLocation
    val permissionsGranted = PackageInfo().permissions
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        pckMangr.longVersionCode
    } else {
        pckMangr.versionCode
    }
    val androidVersion = Build.VERSION.RELEASE
    val time = Build.TIME
    val user = Build.USER
    val freeSpace = Environment.getDataDirectory().freeSpace
    val totalSpace = Environment.getDataDirectory().totalSpace
    val timeZone = TimeZone.getDefault().id
    val unknown = Build.UNKNOWN
    val type = Build.TYPE
    val fingerprint = Build.FINGERPRINT
    val hardware = Build.HARDWARE
    val host = Build.HOST
    val display = Build.DISPLAY
    val tags = Build.TAGS
    val timeNow =
        SimpleDateFormat(("EEE, dd mm yyyy, HH:mm aaa Z"), Locale.ENGLISH).format(Date()).toString()
    val hashMapReturnable = hashMapOf(
        "APP NAME" to "Hiphe",
        "USER" to user,
        "TIMEZONE" to timeZone,
        "Fingerprint" to "$fingerprint",
        "Hardware" to "$hardware",
        "Host" to "$host",
        "Display" to "$display",
        "TAGS" to "$tags",
        "MODEL" to "$model",
        "MANUFACTURER" to "$manufacturer",
        "USER" to "$user",
        "ID" to "$id",
        "DEVICE" to "$device",
        "BRAND" to "$brand",
        "PRODUCT" to "$product",
        "Android SDK Version" to "$androidOs",
        "Android Release Version" to "$androidVersion",
        "Free Space" to "$freeSpace",
        "Total Space" to "$totalSpace",
        "Package Name" to "$packageName",
        "Version Name" to "$versionName",
        "CURRENT TIMESTAMP" to "$timeNow"
    )

    return hashMapReturnable
}

fun getRunningApps(context: Context): String {
    return try {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses.toString()
    } catch (e: Exception) {
        "$e"
    }
}