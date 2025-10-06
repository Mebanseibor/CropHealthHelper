package lpu.semesterseven.project.testtwo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityTesttwoBinding

class TestTwoActivity: AppCompatActivity() {
    // binding
    private lateinit var binding: ActivityTesttwoBinding

    // buttons
    private lateinit var btnStart   : Button
    private lateinit var btnStop    : Button

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_testtwo)

        btnStart    = binding.btnStart
        btnStop     = binding.btnStop

        btnStart.setOnClickListener {
            Intent(this, DownloadService::class.java).also{
                startService(it)
            }
        }
        btnStop.setOnClickListener {
            Intent(this, DownloadService::class.java).also{
                stopService(it)
            }
        }
    }
}