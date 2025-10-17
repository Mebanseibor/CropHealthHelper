package lpu.semesterseven.project.notes.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application){
    private val repository: NoteRepository
    var allNotes: LiveData<List<Note>>

    init{
        val dao = NotesDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(dao)
        allNotes = repository.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch{ repository.insert(note) }
    fun update(note: Note) = viewModelScope.launch{ repository.update(note) }
    fun delete(note: Note) = viewModelScope.launch{ repository.delete(note) }
}