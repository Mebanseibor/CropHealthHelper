package lpu.semesterseven.project.notes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val NOTE_ACTION_BUTTON_EDIT   = 0
const val NOTE_ACTION_BUTTON_DELETE = 1

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var desc: String,
    @ColumnInfo(defaultValue="0") val timestamp: Long = System.currentTimeMillis()
)