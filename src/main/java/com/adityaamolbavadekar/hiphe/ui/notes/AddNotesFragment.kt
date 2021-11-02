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
import androidx.lifecycle.*
import com.adityaamolbavadekar.hiphe.Hiphe
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import com.adityaamolbavadekar.hiphe.room.note.NoteDao
import com.adityaamolbavadekar.hiphe.room.note.NotesDataClass
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddNotesFragment : Fragment() {

    private lateinit var addNotesViewModel: AddNotesViewModel
    private lateinit var titleEditText: EditText
    private lateinit var bodyEditText: EditText
    private val viewModel: AddNotesViewModel by activityViewModels {
        AddNotesViewModelFactory(
            (activity?.application as Hiphe).notesDatabase.noteDao()
        )
    }

    class AddNotesViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddNotesViewModel::class.java)) {

                @Suppress("UNCHECKED_CAST")
                return AddNotesViewModel(noteDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }

    class AddNotesViewModel(private val noteDao: NoteDao) : ViewModel() {
        init {
            HipheInfoLog(TAG, "AddNotesViewModel created!")
        }

        private val _titleText = MutableLiveData<String>().apply {
            value = null
        }
        val titleText: LiveData<String> = _titleText

        fun setTitleText(title: String) {
            _titleText.value = title
        }

        private val _bodyText = MutableLiveData<String>().apply {
            value = null
        }
        val bodyText: LiveData<String> = _bodyText

        fun setBodyText(title: String) {
            _bodyText.value = title
        }


        override fun onCleared() {
            super.onCleared()
            HipheInfoLog(TAG, "AddNotesViewModel destroyed!")
        }

        fun create(noteTitle: String, noteBody: String) {
            val createdOn =
                SimpleDateFormat("dd/mm/yyyy HH:mm aaa Z ", Locale.ENGLISH).format(Date())
                    .toString()
            val editedOn =
                SimpleDateFormat("dd/mm/yyyy HH:mm aaa Z ", Locale.ENGLISH).format(Date())
                    .toString()
            val notesDataClass = NotesDataClass(0, noteTitle, noteBody, createdOn, editedOn, 0)

            viewModelScope.launch {
                noteDao.insertTheNote(notesDataClass)
            }
        }

        companion object {
            const val TAG = "AddNotesViewModel"
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addNotesViewModel = ViewModelProviders.of(this).get(AddNotesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_note, container, false)
        titleEditText = root.findViewById(R.id.titleEditText)
        bodyEditText = root.findViewById(R.id.bodyEditText)

        viewModel.titleText.observe(this.viewLifecycleOwner, {
            titleEditText.setText(it)
        })

        viewModel.bodyText.observe(this.viewLifecycleOwner, {
            bodyEditText.setText(it)
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
        if (titleEditText.text != null || bodyEditText.text != null) {
            viewModel.apply {
                create(titleEditText.text.toString(), bodyEditText.text.toString())
                setTitleText(titleEditText.text.toString())
                setBodyText(bodyEditText.text.toString())
            }
        }
    }


}
