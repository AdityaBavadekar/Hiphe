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

package com.adityaamolbavadekar.hiphe.ui.notes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityaamolbavadekar.hiphe.interaction.HipheInfoLog
import com.adityaamolbavadekar.hiphe.room.note.NoteDao
import com.adityaamolbavadekar.hiphe.room.note.NotesDataClass
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NotesViewModel(private val noteDao: NoteDao) : ViewModel() {
    init {
        HipheInfoLog(TAG, "NotesViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        HipheInfoLog(TAG, "NotesViewModel destroyed!")
    }

    var defaultExampleNotes = listOf(
        NotesDataClass(0,"Welcome to Hiphe Notes","This is how you can use Hiphe Notes for noting thing in a creative way!","","",0)
    )

    fun areNotesNull(notes: Int): Boolean {
        return notes == 0
    }

    fun create(noteTitle: String, noteBody: String) {
        val createdOn =
            SimpleDateFormat("dd/mm/yyyy HH:mm aaa Z ", Locale.ENGLISH).format(Date()).toString()
        val editedOn =
            SimpleDateFormat("dd/mm/yyyy HH:mm aaa Z ", Locale.ENGLISH).format(Date()).toString()
        val notesDataClass = NotesDataClass(0, noteTitle, noteBody, createdOn, editedOn, 0)

        viewModelScope.launch {
            noteDao.insertTheNote(notesDataClass)
        }
    }

    fun getAllTheNotes(): LiveData<List<NotesDataClass>>  = noteDao.getAllNotes()

    fun getSpecificNoteFromId(indexId: Long): LiveData<NotesDataClass> = noteDao.getTheNotesFromId(indexId)

    fun updateTheNote(notesDataClass: NotesDataClass) {
        viewModelScope.launch {
            noteDao.updateTheNote(notesDataClass)
        }
    }

    fun deleteTheNoteWithId(indexId: Long) {
        viewModelScope.launch {
            noteDao.deleteNote(indexId)
        }
    }

    fun deleteAllNotes(notesDataClass: NotesDataClass) {
        viewModelScope.launch {
            noteDao.deleteAllNotes()
        }
    }


    companion object {
        const val TAG = "NotesViewModel"
    }
}
