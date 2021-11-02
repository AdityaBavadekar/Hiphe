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

package com.adityaamolbavadekar.hiphe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheErrorLog
import com.adityaamolbavadekar.hiphe.models.FaqAdapter
import java.util.*

class FaqsFragment : Fragment() {
    private var sampleListFAQsClone: MutableList<FaqAdapter.FAQ> = sampleListFAQs
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var recyclerViewAdapter: FaqAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_faqs, container, false)
        sampleListFAQsClone.addAll(sampleListFAQs)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerViewAdapter = FaqAdapter(requireActivity(), sampleListFAQsClone)
        recyclerView.adapter = recyclerViewAdapter
        try {
            searchView = root.findViewById(R.id.search_view)
            searchViewSetOnQueryTextListener()
        } catch (e: Exception) {
            HipheErrorLog(TAG, "Error adding TextListener on Search View : ", e.toString())
        }
        return root
    }

    private fun searchViewSetOnQueryTextListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //START
                sampleListFAQsClone.clear()//CLEAR
                if (newText != null) {
                    val text = newText.toLowerCase(Locale.getDefault())

                    if (text.isNotEmpty()) {
                        sampleListFAQs.forEach { FAQ ->
                            if (FAQ.titleText.toLowerCase(Locale.getDefault())
                                    .contains(text) || FAQ.subTitleText.toLowerCase(Locale.getDefault())
                                    .contains(text) || FAQ.tags.toString()
                                    .toLowerCase(Locale.getDefault()).contains(text)
                            ) {
                                sampleListFAQsClone.add(FAQ)
                            } else {
                                val element = FaqAdapter.FAQ("No item was found", "", listOf())
                                sampleListFAQsClone.add(element)
                            }
                            recyclerViewAdapter.notifyDataSetChanged()
                        }
                    } else {

                        sampleListFAQsClone.clear()
                        sampleListFAQsClone.addAll(sampleListFAQs)

                    }
                } else {

                    sampleListFAQsClone.clear()
                    sampleListFAQsClone.addAll(sampleListFAQs)
                    recyclerViewAdapter.notifyDataSetChanged()

                }
                return false
                //END
            }
        }
        )
    }

    override fun onStart() {
        super.onStart()
        if (sampleListFAQsClone.isEmpty()) {
            sampleListFAQsClone.addAll(sampleListFAQs)
        }
    }

    companion object {
        const val TAG = "FaqsFragment"
    }


}