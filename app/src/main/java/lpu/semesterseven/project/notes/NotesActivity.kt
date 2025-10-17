package lpu.semesterseven.project.notes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import lpu.semesterseven.project.R
import lpu.semesterseven.project.components.toolbar.NotesToolBar
import lpu.semesterseven.project.databinding.ActivityNotesBinding
import lpu.semesterseven.project.notes.database.NoteViewModel
import lpu.semesterseven.project.notes.fragments.NotesEmptyFragment
import lpu.semesterseven.project.notes.fragments.NotesListFragment

class NotesActivity: AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityNotesBinding

    private val viewModel           : NoteViewModel by viewModels()

    // views
    private lateinit var toolbar    : NotesToolBar
    private lateinit var btnAdd     : FloatingActionButton

    // notes list
    private lateinit var frgMgr     : FragmentManager
    private lateinit var notesFrame : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notes)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        frgMgr      = supportFragmentManager
        notesFrame  = binding.notesFrame

        viewModel.allNotes.observe(this) { allNotes ->
            val transaction = frgMgr.beginTransaction()
            var frg: Fragment = if (allNotes.isNullOrEmpty()) NotesEmptyFragment() else NotesListFragment()
            transaction.replace(notesFrame.id, frg)
            transaction.commit()
        }

        btnAdd = binding.btnAdd
        btnAdd.setOnClickListener {
            startActivity(Intent(this, CreateNotesActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        toolbar.init()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = toolbar.onOptionsItemSelected(item)
}