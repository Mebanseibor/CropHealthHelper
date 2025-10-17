package lpu.semesterseven.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import lpu.semesterseven.project.components.toolbar.MainToolBar
import lpu.semesterseven.project.databinding.ActivityMainBinding
import lpu.semesterseven.project.gallery.GalleryActivity
import lpu.semesterseven.project.helplines.HelplinesActivity
import lpu.semesterseven.project.serverinfo.headlines.HeadlinesFragment
import lpu.semesterseven.project.notes.NotesActivity
import lpu.semesterseven.project.test.TestActivity
import lpu.semesterseven.project.testmedia.MediaPlayerActivity
import lpu.semesterseven.project.testtwo.TestTwoActivity

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityMainBinding

    // fragment handler
    private lateinit var fragMng    : FragmentManager

    // buttons
    private lateinit var btnGallery     : ImageButton
    private lateinit var btnHelpLines   : ImageButton
    private lateinit var btnTest        : Button
    private lateinit var btnMusicPlayer : Button
    private lateinit var btnDownloader  : Button
    private lateinit var btnNotes       : ImageButton

    // views
    private lateinit var headlinesLayout: FrameLayout
    private lateinit var toolbar        : MainToolBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fragMng = supportFragmentManager
        initButtons()
        initHeadlines()

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun initButtons(){
        btnGallery      = binding.btnGallery
        btnTest         = binding.btnTest
        btnMusicPlayer  = binding.btnMusicPlayer
        btnDownloader   = binding.btnDownloader
        btnHelpLines    = binding.btnHelpLines
        btnNotes        = binding.btnNotes

        btnGallery.setOnClickListener       { startActivity(Intent(this, GalleryActivity::class.java)) }
        btnHelpLines.setOnClickListener     { startActivity(Intent(this, HelplinesActivity::class.java)) }
        btnTest.setOnClickListener          { startActivity(Intent(this, TestActivity::class.java)) }
        btnMusicPlayer.setOnClickListener   { startActivity(Intent(this, MediaPlayerActivity::class.java)) }
        btnDownloader.setOnClickListener    { startActivity(Intent(this, TestTwoActivity::class.java)) }
        btnNotes.setOnClickListener         { startActivity(Intent(this, NotesActivity::class.java)) }

        btnTest.visibility          = View.GONE
        btnMusicPlayer.visibility   = View.GONE
        btnDownloader.visibility    = View.GONE
    }

    private fun initHeadlines(){
        headlinesLayout = binding.headlinesFrameLayout

        var t = fragMng.beginTransaction()
        t.replace(headlinesLayout.id, HeadlinesFragment())
        t.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        toolbar.init()
        toolbar.disableNavigationIcon()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = toolbar.onOptionsItemSelected(item)
}