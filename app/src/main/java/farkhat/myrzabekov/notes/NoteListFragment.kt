package farkhat.myrzabekov.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import farkhat.myrzabekov.notes.databinding.FragmentNoteListBinding

class NoteListFragment : Fragment() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var binding: FragmentNoteListBinding
    private lateinit var adapter: NoteAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NoteAdapter { note ->
            val action =
                NoteListFragmentDirections.actionNoteListFragmentToNoteDetailFragment(note.id)
            firebaseAnalytics.logEvent("view_note", null)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        noteViewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            notes?.let { adapter.submitList(it) }
        })

        binding.fabAddNote.setOnClickListener {
            val action = NoteListFragmentDirections
                .actionNoteListFragmentToNoteDetailFragment(isNewNote = true)
            findNavController().navigate(action)
        }

        return binding.root
    }
}
