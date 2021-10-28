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
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.objects.FAQ

class FaqInfoFragment : Fragment() {

    private lateinit var titleTextView: TextView
    private lateinit var subjectTextView: TextView
    private lateinit var likeButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_faq_info_layout, container, false)
        val title = FAQ.title
        val subtitle = FAQ.subtitle
        titleTextView = root.findViewById(R.id.titleTextView)
        subjectTextView = root.findViewById(R.id.subjectTextView)
        likeButton = root.findViewById(R.id.like_button)
        titleTextView.text = title
        subjectTextView.text = subtitle
        likeButton.setOnClickListener {
            likeButton.setImageResource(R.drawable.ic_baseline_thumb_up_24)
        }
        return root
    }
}