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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.protobuf.StringValue


@Entity(tableName = "hiphe_notes_table")
data class NotesDataClass(
    @PrimaryKey(autoGenerate = true)
    var indexId: Long,
    @ColumnInfo(name = "Title")
    var noteTitle: String?,
    @ColumnInfo(name = "Body")
    var noteBody: String?,
    var createdOn: String,
    var editedOn: String?,
    var attachments: Long = 0
)