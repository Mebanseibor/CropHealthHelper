package lpu.semesterseven.project.notes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.databinding.FragmentNotesBinding
import lpu.semesterseven.project.notes.database.NoteViewModel
import lpu.semesterseven.project.notes.NotesAdapter

class NotesListFragment(): Fragment() {
    private val viewModel: NoteViewModel by viewModels()

    // binding
    private var _binding    : FragmentNotesBinding? = null
    private val binding get() = _binding!!

    // recycler view
    private lateinit var adapter    : NotesAdapter
    private lateinit var rv         : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)

        initNotes()
        return binding.root
    }

    private fun initNotes(){
        rv = binding.myNotesRV
        adapter = NotesAdapter(requireActivity(), viewModel)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(requireContext())
    }
}