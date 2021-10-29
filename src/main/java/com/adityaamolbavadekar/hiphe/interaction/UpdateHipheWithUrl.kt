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

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.adityaamolbavadekar.hiphe.HipheSettingsActivity
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.models.ChangeLogInfo

class UpdateHipheWithUrl {

    fun updateHipheWithChangeInfo(activity: Context, changeLogInfo: ChangeLogInfo) {
        val versionName = changeLogInfo.versionName
        val versionCode = changeLogInfo.versionCode
        val releaseNotes = changeLogInfo.releaseNotes
        val apkURL = changeLogInfo.apkURL
        val apkUri = Uri.parse(apkURL)

        //Download APK
        val downloadManager =
            activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(apkUri)
        request.setTitle(activity.getString(R.string.updating_hiphe))
        request.setDescription(activity.getString(R.string.we_will_download_apk_in_background))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationUri(activity.filesDir.toUri())
        request.allowScanningByMediaScanner()

        val completion = downloadManager.enqueue(request)

        activity.showLongToast(
            downloadManager.getUriForDownloadedFile(completion).toString()
        )
        try {
            downloadManager.openDownloadedFile(completion)
        } catch (e: Exception) {
            HipheErrorLog(
                HipheSettingsActivity.TAG,
                activity.getString(R.string.error_opening_downloaded_file),
                e.toString()
            )
        }

    }


}