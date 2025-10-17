package lpu.semesterseven.project.notes.database

import androidx.lifecycle.LiveData

class NoteRepository(private val dao: NoteDao) {
    val allNotes: LiveData<List<Note>> = dao.getAllNotes()

    suspend fun insert(note: Note) = dao.insert(note)
    suspend fun update(note: Note) = dao.update(note)
    suspend fun delete(note: Note) = dao.delete(note)
}