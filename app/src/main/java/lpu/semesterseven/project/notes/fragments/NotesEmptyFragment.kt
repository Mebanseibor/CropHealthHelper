package lpu.semesterseven.project.notes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import lpu.semesterseven.project.databinding.FragmentEmptyNotesResultsBinding

class NotesEmptyFragment: Fragment() {
    // binding
    private var _binding   : FragmentEmptyNotesResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmptyNotesResultsBinding.inflate(inflater, container, false)
        return binding.root
    }
}