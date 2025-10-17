package lpu.semesterseven.project.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.R
import lpu.semesterseven.project.components.toolbar.CreateNotesToolBar
import lpu.semesterseven.project.databinding.ActivityCreatenotesBinding
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_WARNING
import lpu.semesterseven.project.dialogs.promptAcknowledgement
import lpu.semesterseven.project.notes.database.Note
import lpu.semesterseven.project.notes.database.NoteViewModel

class CreateNotesActivity: AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityCreatenotesBinding

    private val viewModel           : NoteViewModel by viewModels()

    // views
    private lateinit var toolbar    : CreateNotesToolBar
    private lateinit var btnSubmit  : ImageButton
    private lateinit var etTitle    : EditText
    private lateinit var etDesc     : EditText

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_createnotes)

        initInput()

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun initInput(){
        btnSubmit   = binding.btnSubmit
        etTitle     = binding.title
        etDesc      = binding.desc
        btnSubmit.setOnClickListener{
            val title   : String = etTitle.text.toString().trim()
            val desc    : String = etDesc.text.toString().trim()
            if(title.trim().isEmpty() || desc.trim().isEmpty()){
                promptAcknowledgement(this, "Missing fields", "Notes requires valid titles and description", ACKNOWLEDGEMENT_STATUS_WARNING)
                return@setOnClickListener
            }

            val note = Note(title = title, desc = desc)
            viewModel.insert(note)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return toolbar.init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = toolbar.onOptionsItemSelected(item)
}