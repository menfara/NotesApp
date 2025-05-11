package farkhat.myrzabekov.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NoteDatabase.getDatabase(application).noteDao()
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics

    fun addNote(note: Note) {
        firebaseAnalytics.logEvent("add_note", null)
        viewModelScope.launch {
            noteDao.insert(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteDao.update(note)
        }
    }

    fun deleteNote(note: Note) {
        firebaseAnalytics.logEvent("remove_note", null)
        viewModelScope.launch {
            noteDao.delete(note)
        }
    }
}