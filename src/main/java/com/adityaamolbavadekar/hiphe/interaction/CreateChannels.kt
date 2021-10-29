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

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class CreateChannels() {

    data class Channel(
        var CHANNEL_ID: String,
        val CHANNEL_DESCRIPTION: String,
        val CHANNEL_IMPORTANCE: Channel.Importance,
        val CHANNEL_GROUP: String?
    ) {
        enum class Importance {
            High, Low, Medium, Min, Max
        }
    }

    data class ChannelGroup(
        var CHANNEL_ID: String,
        var CHANNEL_NAME: String
    )

    class Builder(private val context: Context) {
        private var notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private var channelsSystem = notificationManager.notificationChannels
        private var channelGroupsSystem = notificationManager.notificationChannelGroups


        fun createChannels(channels: List<Channel>) {
            channels.forEach { channel: Channel ->
                if (!channelWasCreated(channel)) {
                    createNotificationChannel(
                        channel.CHANNEL_ID,
                        channel.CHANNEL_DESCRIPTION,
                        channel.CHANNEL_IMPORTANCE,
                        channel.CHANNEL_GROUP
                    )
                }
            }
        }

        private fun channelWasCreated(channel: Channel): Boolean {
            val importance = when (channel.CHANNEL_IMPORTANCE) {
                Channel.Importance.High -> NotificationManager.IMPORTANCE_HIGH
                Channel.Importance.Medium -> NotificationManager.IMPORTANCE_DEFAULT
                Channel.Importance.Low -> NotificationManager.IMPORTANCE_LOW
                Channel.Importance.Min -> NotificationManager.IMPORTANCE_MIN
                Channel.Importance.Max -> NotificationManager.IMPORTANCE_MAX
            }
            val item = NotificationChannel(channel.CHANNEL_ID, channel.CHANNEL_ID, importance)
            return channelsSystem.contains(item)
        }

        private fun createNotificationChannel(
            channelID: String,
            channelDescription: String,
            channelImportance: Channel.Importance,
            channelGroup: String?
        ) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val importance = when (channelImportance) {
                Channel.Importance.High -> NotificationManager.IMPORTANCE_HIGH
                Channel.Importance.Medium -> NotificationManager.IMPORTANCE_DEFAULT
                Channel.Importance.Low -> NotificationManager.IMPORTANCE_LOW
                Channel.Importance.Min -> NotificationManager.IMPORTANCE_MIN
                Channel.Importance.Max -> NotificationManager.IMPORTANCE_MAX
            }

            val channel = if (channelGroup == null) {
                NotificationChannel(
                    channelID,
                    channelID,
                    importance
                ).apply {
                    description = channelDescription
                }
            } else {
                NotificationChannel(
                    channelID,
                    channelID,
                    importance
                ).apply {
                    description = channelDescription
                    this.group = channelGroup
                }
            }
            // Register the channel with the system
//            notificationManager.get
            notificationManager.createNotificationChannel(channel)
        }


        fun createChannelGroups(channelGroups: List<ChannelGroup>) {
            channelGroups.forEach { channelGroup: ChannelGroup ->
                if (!channelGroupWasCreated(channelGroup)) {
                    createNotificationChannelGroup(
                        channelGroup.CHANNEL_ID,
                        channelGroup.CHANNEL_NAME
                    )
                }
            }
        }

        private fun channelGroupWasCreated(channelGroup: ChannelGroup): Boolean {
            val item = NotificationChannelGroup(channelGroup.CHANNEL_ID, channelGroup.CHANNEL_NAME)
            return channelGroupsSystem.contains(item)
        }

        private fun createNotificationChannelGroup(channelId: String, channelName: String) {
            val channelGroup = NotificationChannelGroup(channelId, channelName)
            // Register the channel group with the system
            notificationManager.createNotificationChannelGroup(channelGroup)
        }
    }

    class Creator(private val context: Context) {

        private var notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        private object ObjChannel {
            var CHANNEL_ID: String = ""
            var CHANNEL_TITLE: String = ""
            var CHANNEL_DESCRIPTION: String = ""
            var CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
            var CHANNEL_GROUP: String? = null
        }

        fun setChannelId(channelId: String) {
            ObjChannel.CHANNEL_ID = channelId
        }

        fun setChannelDescription(channelDescription: String) {
            ObjChannel.CHANNEL_DESCRIPTION = channelDescription
        }

        fun setChannelTitle(channelTitle: String) {
            ObjChannel.CHANNEL_TITLE = channelTitle
        }

        fun setChannelImportance(channelImportance: Int) {
            ObjChannel.CHANNEL_IMPORTANCE = channelImportance
        }

        fun setChannelGroup(channelGroup: String?) {
            ObjChannel.CHANNEL_GROUP = channelGroup
        }

        fun create() {

            val item = if (ObjChannel.CHANNEL_GROUP != null) {
                NotificationChannel(
                    ObjChannel.CHANNEL_ID,
                    ObjChannel.CHANNEL_TITLE,
                    ObjChannel.CHANNEL_IMPORTANCE
                ).apply {
                    this.description = ObjChannel.CHANNEL_DESCRIPTION
                    this.group = ObjChannel.CHANNEL_GROUP
                }
            } else {
                NotificationChannel(
                    ObjChannel.CHANNEL_ID,
                    ObjChannel.CHANNEL_TITLE,
                    ObjChannel.CHANNEL_IMPORTANCE
                ).apply {
                    this.description = ObjChannel.CHANNEL_DESCRIPTION
                }
            }
            if (!notificationManager.notificationChannels.contains(item)) {
                notificationManager.createNotificationChannel(item)
            }
        }
    }

}