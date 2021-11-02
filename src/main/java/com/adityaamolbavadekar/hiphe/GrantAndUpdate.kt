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

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.adityaamolbavadekar.hiphe.interaction.showLongToast
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.adityaamolbavadekar.hiphe.models.ChangeLogInfo

class GrantAndUpdate(
    private val requireActivity: FragmentActivity,
    private val changeLogInfo: ChangeLogInfo
) {

    fun start(){
        requestPermissionForWrite()
    }

    private var r =
        requireActivity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            validatePermission(isGranted)
        }

    private fun validatePermission(granted: Boolean) {
        if (granted) downloadUpdate()
        else requireActivity.showToast("This permission is required for automatically updating")
    }

    private fun downloadUpdate() {
        val apkUrl = Uri.parse(changeLogInfo.apkURL)
        val versionName = changeLogInfo.versionName
        val fileName = "Hiphe-$versionName"

        val downloadManager =
            requireActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(apkUrl)
        request.setTitle(requireActivity.getString(R.string.updating_hiphe))
        request.setDescription(requireActivity.getString(R.string.we_will_download_apk_in_background))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.allowScanningByMediaScanner()

        val completion = downloadManager.enqueue(request)

        requireActivity.showLongToast(
            downloadManager.getUriForDownloadedFile(completion).toString()
        )

        try {
            downloadManager.openDownloadedFile(completion)
            requireActivity.showToast("After downloading click Install")
        } catch (e: Exception) {
            HipheErrorLog(
                HipheSettingsActivity.TAG,
                requireActivity.getString(R.string.error_opening_downloaded_file),
                e.toString()
            )
        }
    }

    private fun requestPermissionForWrite() {
        when (ContextCompat.checkSelfPermission(
            requireActivity,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )) {
            PackageManager.PERMISSION_DENIED -> {
                r.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            PackageManager.PERMISSION_GRANTED -> {
                downloadUpdate()
            }
        }
    }
}
