package lpu.semesterseven.project.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NotesDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${notes_table_name} ADD COLUMN timestamp INTEGER NOT NULL DEFAULT")
            }
        }

        fun getDatabase(context: Context): NotesDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder( context.applicationContext, NotesDatabase::class.java,
                    notes_table_name
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}