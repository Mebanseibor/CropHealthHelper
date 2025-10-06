package lpu.semesterseven.project.settings

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.R
import lpu.semesterseven.project.components.toolbar.SettingsToolBar
import lpu.semesterseven.project.databinding.ActivitySettingsBinding
import lpu.semesterseven.project.network.NetworkActivity

class SettingsActivity: AppCompatActivity() {
    // binding
    private lateinit var binding: ActivitySettingsBinding

    // views
    private lateinit var toolbar: SettingsToolBar

    // buttons
    private lateinit var btnNetwork     : ImageButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        initButtons()
    }

    private fun initButtons(){
        btnNetwork      = binding.btnNetwork

        btnNetwork.setOnClickListener       { startActivity(Intent(this, NetworkActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return toolbar.init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = toolbar.onOptionsItemSelected(item)
}