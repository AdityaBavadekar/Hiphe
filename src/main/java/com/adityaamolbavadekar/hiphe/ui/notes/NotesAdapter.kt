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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.room.note.NotesDataClass
import io.grpc.NameResolver

class NotesAdapter(
    private val notes: List<NotesDataClass>,
    private val onItemClicked1: FragmentActivity
) :
    RecyclerView.Adapter<NotesAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val bodyTextView: TextView = itemView.findViewById(R.id.bodyTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = notes[position]
        holder.apply {
            titleTextView.text = currentItem.noteTitle
            bodyTextView.text = currentItem.noteBody
            titleTextView.setOnClickListener {
//                navigateToEditFragment(currentItem)
                val extras = FragmentNavigatorExtras(titleTextView to "editTextTitleBig", bodyTextView to "editTextBodyBig")

            }
            bodyTextView.setOnClickListener {
                navigateToEditFragment(currentItem)
            }
        }
    }

    private fun navigateToEditFragment(currentItem: NotesDataClass) {
//        val noteTitleArgs: NameResolver.Args = EditNoteFragment by onItemClicked1.navArgs()
//        onItemClicked1.findNavController(R.id.nav_host_fragment).navigate(R.id.action_notesFragment_to_editNoteFragment,null,null,extras)
//        val extras = FragmentNavigatorExtras(titleTextView to "editTextTitleBig", bodyTextView to "editTextBodyBig")


    }

    override fun getItemCount(): Int = notes.size

}
