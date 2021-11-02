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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adityaamolbavadekar.hiphe.Hiphe
import com.adityaamolbavadekar.hiphe.R
import com.adityaamolbavadekar.hiphe.room.note.NotesDataClass
import com.adityaamolbavadekar.hiphe.ui.notes.view_model.NotesViewModel
import com.adityaamolbavadekar.hiphe.ui.notes.view_model.NotesViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragment : Fragment() {

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewPinned: RecyclerView
    private lateinit var fabNewNote: FloatingActionButton
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
        val root = inflater.inflate(R.layout.fragment_notes_main, container, false)

        recyclerView = root.findViewById(R.id.notesRecyclerView)
        recyclerView = root.findViewById(R.id.pinnedNotesRecyclerView)
        fabNewNote = root.findViewById(R.id.fabNewNote)

        fabNewNote.setOnClickListener {
            requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.action_notesFragment_to_addNotesFragment)
        }

        viewModel.getAllTheNotes().observe(this.viewLifecycleOwner, { notes ->
            if (viewModel.areNotesNull(notes.size)) {
                val notesToBeShown = viewModel.defaultExampleNotes
                setUpRecyclerViewWith(notesToBeShown)
            } else {
                setUpRecyclerViewWith(notes)
            }
        })
        return root
    }

    private fun setUpRecyclerViewWith(notes: List<NotesDataClass>) {
        recyclerView.adapter = NotesAdapter(notes, this)
    }

    companion object {
        const val TAG = "NotesFragment"
    }

}