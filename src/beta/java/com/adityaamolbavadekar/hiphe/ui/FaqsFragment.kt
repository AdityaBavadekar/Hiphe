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
import com.adityaamolbavadekar.hiphe.models.FaqAdapter
import java.util.*

class FaqsFragment : Fragment() {

    private val sampleListFAQs: MutableList<FaqAdapter.FAQ> = mutableListOf(
        FaqAdapter.FAQ(
            "What is iVR truth?",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is created on?",
            "Created on is form of date and time when your account was created. You can find this property in Account Info category. This is system generated value and cannot be modified. Other users are able to see this value in name of \"Joined :\"",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is USER ID",
            "USER ID is a unique value that is assigned to your account by automated system when you first sign up. You can find this property in Account Info category. This is system generated value and cannot be modified. This value may be/may not be shared to Hiphe users but this is not a credential to login. This is just an unique value that identifies you on Hiphe Users Platform",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.UserId,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is EMAIL ADDRESS",
            "EMAIL ADDRESS is just same as what it tells, it is the Email Address badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        ),
        FaqAdapter.FAQ(
            "What is iVR truth",
            "iVR truth is a badge that an user gets attached on his/her profile only and only when he/she verifies his Email Address by clicking link sent to your Email Address",
            listOf(
                FaqAdapter.FAQ.FaqTAGs.Account,
                FaqAdapter.FAQ.FaqTAGs.Verification,
                FaqAdapter.FAQ.FaqTAGs.Information
            )
        )
    )
    private var sampleListFAQsClone: MutableList<FaqAdapter.FAQ> = sampleListFAQs
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_faqs, container, false)
        recyclerView = root.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = FaqAdapter(sampleListFAQsClone)
        recyclerView.adapter = adapter
        try {
            searchView = root.findViewById(R.id.search_view)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        val text = newText.toUpperCase(Locale.ROOT)
                        sampleListFAQsClone.clear()
                        adapter.notifyDataSetChanged()
                        sampleListFAQs.forEach {
                            if (it.titleText.contains(text) || it.subTitleText.contains(text)) {
                                sampleListFAQsClone.add(it)
                                adapter.notifyDataSetChanged()
                            } else {
                                val element = FaqAdapter.FAQ("No item was found", "", listOf())
                                sampleListFAQsClone.add(element)
                            }
                        }

                    } else {
                        sampleListFAQsClone = sampleListFAQs
                        adapter.notifyDataSetChanged()
                    }
                    return true
                }
            })
        } catch (e: Exception) {
        }



        return root
    }
}