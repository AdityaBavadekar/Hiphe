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

package com.adityaamolbavadekar.hiphe.models

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.adityaamolbavadekar.hiphe.R

class FaqAdapter(private val activity: Activity, private val itemIndexList: MutableList<FAQ>) :
    RecyclerView.Adapter<FaqAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.text1)
        val subTitleTextView: TextView = itemView.findViewById(R.id.text2)
        val tagsTextView: TextView = itemView.findViewById(R.id.textTags)

    }

    data class FAQ(
        val titleText: String,
        val subTitleText: String,
        val tags: List<FaqTAGs>
    ) {
        enum class FaqTAGs {
            Files, Account, Data, Logout, Information, Crash, ReportUser, SendLogs, AppTheme, Error, Verification, UserId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.faq_list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemIndexList[position]
        holder.apply {
            titleTextView.text = currentItem.titleText
            subTitleTextView.text = currentItem.subTitleText
            var text = "TAGS : "
            currentItem.tags.forEach {
                text += it.name + ", "
            }
            val finalText = text.removeSuffix(", ")
            tagsTextView.text = finalText

            titleTextView.setOnClickListener {
                defineFaqOnClickListener(currentItem.titleText, currentItem.subTitleText)
            }

            subTitleTextView.setOnClickListener {
                defineFaqOnClickListener(currentItem.titleText, currentItem.subTitleText)
            }
            subTitleTextView.setOnClickListener {
                defineFaqOnClickListener(currentItem.titleText, currentItem.subTitleText)
            }
        }
    }

    private fun defineFaqOnClickListener(titleText: String, subTitleText: String) {
        com.adityaamolbavadekar.hiphe.objects.FAQ.title = titleText
        com.adityaamolbavadekar.hiphe.objects.FAQ.subtitle = subTitleText
        Navigation.findNavController(activity, R.id.nav_host_fragment)
            .navigate(R.id.action_settingsFragmentFaqs_to_faqInfoFragment)
    }

    override fun getItemCount(): Int {
        return itemIndexList.size
    }


}