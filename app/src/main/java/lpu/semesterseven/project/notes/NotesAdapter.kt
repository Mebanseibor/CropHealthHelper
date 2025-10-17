package lpu.semesterseven.project.notes

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import lpu.semesterseven.project.R
import lpu.semesterseven.project.dialogs.promptAcknowledgement
import lpu.semesterseven.project.dialogs.promptPositiveNegative
import lpu.semesterseven.project.notes.database.Note
import lpu.semesterseven.project.notes.database.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.CountDownLatch

class NotesAdapter(
        private var context: Context,
        private var viewModel: NoteViewModel
    ) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var notes: List<Note>   = listOf()
    private var mainHandler         = Handler(Looper.getMainLooper())

    init{
        viewModel.allNotes.observe((context as LifecycleOwner)){ notes->
            this.notes = notes
            notifyDataSetChanged()
        }
    }

    inner class NotesViewHolder(view: View): ViewHolder(view){
        var title       : TextView      = view.findViewById<TextView>(R.id.title)
        var desc        : TextView      = view.findViewById<TextView>(R.id.desc)
        var tmsp        : TextView      = view.findViewById<TextView>(R.id.timestamp)
        var btnEdit     : ImageButton   = view.findViewById<ImageButton>(R.id.btnEdit)
        var btnDelete   : ImageButton   = view.findViewById<ImageButton>(R.id.btnDelete)

        fun bind(note: Note){
            title.text   = note.title
            desc.text    = note.desc
            val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            tmsp.text    = sdf.format(Date(note.timestamp))

            btnEdit.setOnClickListener {
                promptAcknowledgement(context, "Cannot edit\n${note.title}", "Editing feature coming soon")
            }
            btnDelete.setOnClickListener {
                Thread{
                    var latch   = CountDownLatch(1)
                    var proceed = false
                    mainHandler.post{
                        promptPositiveNegative(context, "Confirm deletion", "Delete note:\n${note.title}"){ result->
                            proceed = if(result) true else false
                            latch.countDown()
                        }
                    }

                    latch.await()
                    if(!proceed) return@Thread

                    viewModel.delete(note)
                }.start()
            }
        }
    }

    fun loadNotes(notes: List<Note>){
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): NotesViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_notes, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder( holder: NotesViewHolder, position: Int ) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size
}