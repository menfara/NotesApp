package farkhat.myrzabekov.notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import farkhat.myrzabekov.notes.databinding.FragmentNoteDetailBinding

class NoteDetailFragment : Fragment() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var binding: FragmentNoteDetailBinding
    private val args: NoteDetailFragmentArgs by navArgs()
    private var currentNote: Note? = null

    // Declare FirebaseAnalytics instance
//    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize FirebaseAnalytics
//        firebaseAnalytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailBinding.inflate(inflater, container, false)

        if (args.noteId != 0L) {
//            firebaseAnalytics.logEvent("view_note", null)

            noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
                currentNote = notes.find { it.id == args.noteId }
                currentNote?.let {
                    binding.etTitle.setText(it.title)
                    binding.etContent.setText(it.content)
                }
            }
        }

        if (args.isNewNote) {
            binding.etTitle.isEnabled = true
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            if (currentNote == null) { // Saving a new note
                val newNote = Note(title = title, content = content)
                noteViewModel.addNote(newNote)
            } else {
                val updatedNote = currentNote!!.copy(title = title, content = content)
                noteViewModel.updateNote(updatedNote)
            }

            findNavController().navigateUp()
        }

        binding.btnDelete.setOnClickListener {
            currentNote?.let {
                noteViewModel.deleteNote(it)
            }
            findNavController().navigateUp()
        }

        return binding.root
    }
}