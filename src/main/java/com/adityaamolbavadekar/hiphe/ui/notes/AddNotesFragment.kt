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

package com.adityaamolbavadekar.hiphe.ui.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import com.adityaamolbavadekar.hiphe.Hiphe
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.ui.notes.view_model.NotesViewModel
import com.adityaamolbavadekar.hiphe.ui.notes.view_model.NotesViewModelFactory

class AddNotesFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var titleEditText: EditText
    private lateinit var bodyEditText: EditText
    private val viewModel: NotesViewModel by activityViewModels {
        NotesViewModelFactory(
            (activity?.application as Hiphe).notesDatabase.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notesViewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_note, container, false)
        titleEditText = root.findViewById(R.id.titleEditText)
        bodyEditText = root.findViewById(R.id.bodyEditText)

        viewModel.titleText.observe(this.viewLifecycleOwner,{
            titleEditText.setText(it.toString())
        })

        viewModel.bodyText.observe(this.viewLifecycleOwner,{
            bodyEditText.setText(it.toString())
        })

        return root
    }

    companion object {
        const val TAG = "AddNotesFragment"
    }


    override fun onPause() {
        super.onPause()
        validateAndCreateNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        validateAndCreateNote()
    }

    override fun onStop() {
        super.onStop()
        validateAndCreateNote()
    }

    private fun validateAndCreateNote() {
        if (titleEditText.text != null || bodyEditText.text != null){
            viewModel.apply {
                create(titleEditText.text.toString(),bodyEditText.text.toString())
                setTitleText(titleEditText.text.toString())
                setBodyText(bodyEditText.text.toString())
            }
        }
    }


}
