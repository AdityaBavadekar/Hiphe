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

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.adityaamolbavadekar.hiphe.HipheSettingsActivity
import com.adityaamolbavadekar.hiphe.LauncherActivity
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import com.adityaamolbavadekar.hiphe.interaction.showLongToast
import com.adityaamolbavadekar.hiphe.interaction.showToast
import com.adityaamolbavadekar.hiphe.models.ChangeLogInfo
import com.adityaamolbavadekar.hiphe.models.GitRawApi
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HipheUpdateChecker(private val context: Context) : LiveData<Boolean>() {

    override fun onActive() {
        super.onActive()
        checkForUpdates()
    }

    private fun checkForUpdates(){

        makeRequestToGitHubForGettingJson()

    }

    private fun makeRequestToGitHubForGettingJson() {

        val pckMangr = context.packageManager.getPackageInfo(
            context.packageName,
            0
        )
        val buildVersionName = pckMangr.versionName
        try {


            val retrofit = Retrofit.Builder()
                .baseUrl(HipheSettingsActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


            val gitRawApi: GitRawApi = retrofit.create(GitRawApi::class.java)

            val call = gitRawApi.getNewUpdatesCall()

            val callback = object : Callback<List<ChangeLogInfo>> {

                override fun onResponse(
                    call: Call<List<ChangeLogInfo>>,
                    responseResult: Response<List<ChangeLogInfo>>
                ) {

                    if (responseResult.isSuccessful) {

                        onSuccessfulRequest(responseResult,call,buildVersionName)

                    } else {
                        HipheErrorLog(
                            HipheSettingsActivity.TAG,
                            "onResponse: Unsuccessful: ",
                            responseResult.code().toString()
                        )
                        context.showLongToast("Something went wrong...")
                        return
                    }

                }

                override fun onFailure(call: Call<List<ChangeLogInfo>>, t: Throwable) {
                    HipheErrorLog(
                        HipheSettingsActivity.TAG,
                        "onFailure: LOCALIZED MESSAGE: ",
                        " ${t.localizedMessage}"
                    )
                    context.showLongToast("Please check that your Internet Connection is on")
                    HipheErrorLog(
                        HipheSettingsActivity.TAG,
                        "onFailure: STACKTRACE: ",
                        t.stackTraceToString()
                    )
                    HipheErrorLog(
                        HipheSettingsActivity.TAG,
                        "onFailure: DATA: ",
                        t.cause.toString()
                    )
                }

            }

            call.enqueue(callback)
        } catch (e: Exception) {
            HipheErrorLog(
                LauncherActivity.TAG,
                context.getString(R.string.error_making_call_to_changelog_json_url, HipheSettingsActivity.BASE_URL),
                e.toString()
            )
        }

    }

    private fun onSuccessfulRequest(
        responseResult: Response<List<ChangeLogInfo>>,
        call: Call<List<ChangeLogInfo>>,
        buildVersionName: String
    ) {


        val response = responseResult.body()?.get(0)
        val changeLogInfo: ChangeLogInfo? = response
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request was Successful"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET returned $changeLogInfo"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request Release Notes were ${changeLogInfo?.releaseNotes}"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request LatestVersion was ${changeLogInfo?.versionName}"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request LatestVersionCode was ${changeLogInfo?.versionCode}"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request apkURL was ${changeLogInfo?.apkURL}"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request's headers ${responseResult.headers()}"
        )
        HipheInfoLog(
            HipheSettingsActivity.TAG,
            "@GET request's raw ${responseResult.raw()}"
        )

        if (changeLogInfo != null) {
            val versionNameResult = changeLogInfo.versionName
            val versionCodeResult = changeLogInfo.versionCode
            val releaseNotesResult = changeLogInfo.releaseNotes

            if (buildVersionName != versionNameResult) {
                updateHipheWithChangeInfo(changeLogInfo)

//                val b = MaterialAlertDialogBuilder(context)
//                b.setIcon(R.drawable.ic_baseline_system_update_24)
//                b.setTitle("Version $buildVersionName")
//                b.setMessage(
//                    context.getString(
//                        R.string.updated_version_of_hiphe_is_available_formatted,
//                        versionNameResult,
//                        buildVersionName,
//                        versionNameResult,
//                        releaseNotesResult
//                    )
//                )
//                b.setPositiveButton(context.getString(R.string.ok_update_hiphe)) { dialogInterface, _ ->
//                    context.showLongToast("Updating in background, you can continue exploring app")
//                    dialogInterface.dismiss()
//                    try {
//                        updateHipheWithChangeInfo(changeLogInfo)
//                    } catch (e: Exception) {
//                    }
//                }
//                b.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
//                    dialogInterface.dismiss()
//                }
//                b.setCancelable(false)
//                b.create()
//                b.show()

            } else if (buildVersionName == versionNameResult) {
                //DO NOTHING HERE
            }
        }

    }


    private fun updateHipheWithChangeInfo(changeLogInfo: ChangeLogInfo) {
        val versionName = changeLogInfo.versionName
        val versionCode = changeLogInfo.versionCode
        val releaseNotes = changeLogInfo.releaseNotes
        val apkURL = changeLogInfo.apkURL
        val apkUri = Uri.parse(apkURL)
        val fileName = "Hiphe-$versionName"

        context.showLongToast("Hiphe Update Version $versionName is available")
    }

}