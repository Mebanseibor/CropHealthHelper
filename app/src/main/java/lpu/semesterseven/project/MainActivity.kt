package lpu.semesterseven.project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.databinding.ActivityMainBinding
import lpu.semesterseven.project.gallery.GalleryActivity
import lpu.semesterseven.project.network.NetworkActivity

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityMainBinding

    // buttons
    private lateinit var btnGallery : ImageButton
    private lateinit var btnNetwork : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initButtons()
    }

    private fun initButtons(){
        btnGallery  = binding.btnGallery
        btnNetwork  = binding.btnNetwork
        btnGallery.setOnClickListener { startActivity(Intent(this, GalleryActivity::class.java)) }
        btnNetwork.setOnClickListener { startActivity(Intent(this, NetworkActivity::class.java)) }
    }
}