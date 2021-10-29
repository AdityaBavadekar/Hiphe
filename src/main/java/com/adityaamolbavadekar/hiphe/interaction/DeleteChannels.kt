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
class DeleteChannels {


    class Builder(private val context: Context){
        private var notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        private lateinit var channels: List<CreateChannels.Channel>
        private lateinit var channelGroups: List<CreateChannels.ChannelGroup>
        private var channelsSystem = notificationManager.notificationChannels
        private var channelGroupsSystem = notificationManager.notificationChannelGroups

        fun delete(channel: CreateChannels.Channel){
            if (checkChannelCreation(channel)){
                notificationManager.deleteNotificationChannel(channel.CHANNEL_ID)
            }
        }



        fun deleteChannels(channels: List<CreateChannels.Channel>){
            channels.forEach { channel ->
                if (checkChannelCreation(channel)){
                    notificationManager.deleteNotificationChannel(channel.CHANNEL_ID)
                }
            }
        }

        fun deleteChannelGroup(channelGroup: CreateChannels.ChannelGroup){
            if (checkChannelGroupCreation(channelGroup)){
                notificationManager.deleteNotificationChannelGroup(channelGroup.CHANNEL_ID)
            }
        }

        fun deleteChannelGroups(channelGroups: List<CreateChannels.ChannelGroup>){
            channelGroups.forEach { channelGroup ->
                if (checkChannelGroupCreation(channelGroup)){
                    notificationManager.deleteNotificationChannelGroup(channelGroup.CHANNEL_ID)
                }
            }
        }

        private fun checkChannelCreation(channel: CreateChannels.Channel): Boolean {
            val importance = when (channel.CHANNEL_IMPORTANCE) {
                CreateChannels.Channel.Importance.High -> NotificationManager.IMPORTANCE_HIGH
                CreateChannels.Channel.Importance.Medium -> NotificationManager.IMPORTANCE_DEFAULT
                CreateChannels.Channel.Importance.Low -> NotificationManager.IMPORTANCE_LOW
                CreateChannels.Channel.Importance.Min -> NotificationManager.IMPORTANCE_MIN
                CreateChannels.Channel.Importance.Max -> NotificationManager.IMPORTANCE_MAX
            }
            val item = NotificationChannel(channel.CHANNEL_ID, channel.CHANNEL_ID, importance)
            return channelsSystem.contains(item)
        }

        private fun checkChannelGroupCreation(channelGroup: CreateChannels.ChannelGroup): Boolean {
            val item = NotificationChannelGroup(channelGroup.CHANNEL_ID, channelGroup.CHANNEL_NAME)
            return channelGroupsSystem.contains(item)
        }
    }

}