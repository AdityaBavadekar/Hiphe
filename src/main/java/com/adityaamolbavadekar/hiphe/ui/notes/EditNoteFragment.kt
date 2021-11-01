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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog

class EditNoteFragment : Fragment() {

    private lateinit var editNoteViewModel: EditNoteViewModel

    class EditNoteViewModel : ViewModel() {
        init {
            HipheInfoLog(TAG, "EditNoteViewModel created!")
        }

        override fun onCleared() {
            super.onCleared()
            HipheInfoLog(TAG, "EditNoteViewModel destroyed!")
        }

        fun update(notesDataClass: NotesFragment.NotesDataClass) {

        }

        fun delete(notesDataClass: NotesFragment.NotesDataClass) {

        }

        companion object {
            const val TAG = "EditNoteViewModel"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editNoteViewModel = ViewModelProviders.of(this).get(EditNoteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_edit_note, container, false)
        return root
    }

    companion object {
        const val TAG = "EditNoteFragment"
    }
}
