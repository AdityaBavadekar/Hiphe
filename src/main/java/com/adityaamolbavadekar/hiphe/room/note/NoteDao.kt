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
package com.adityaamolbavadekar.hiphe.room.note

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {

    @Query("SELECT * FROM hiphe_notes_table ORDER BY Title ASC")
    fun getAllNotes(): LiveData<List<NotesDataClass>>


    @Query("SELECT * FROM hiphe_notes_table WHERE indexId = :indexId")
    fun getTheNotesFromId(indexId:Long): LiveData<NotesDataClass>

    @Insert
    suspend fun insertTheNote(notesDataClass: NotesDataClass)

    @Update
    suspend fun updateTheNote(notesDataClass: NotesDataClass)

    @Query("DELETE FROM hiphe_notes_table")
    fun deleteAllNotes()

    @Query("DELETE FROM hiphe_notes_table WHERE indexId = :indexId")
    fun deleteNote(indexId: Long)

}