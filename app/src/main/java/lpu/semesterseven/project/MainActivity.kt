package lpu.semesterseven.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.databinding.ActivityMainBinding
import lpu.semesterseven.project.gallery.GalleryActivity
import lpu.semesterseven.project.network.FetchEmergencyNews
import lpu.semesterseven.project.network.NetworkActivity
import lpu.semesterseven.project.serverinfo.parseEmergencyInfo
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityMainBinding

    // buttons
    private lateinit var btnGallery : ImageButton
    private lateinit var btnNetwork : ImageButton

    // views
    private lateinit var headlines  : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initButtons()
        initHeadlines()
    }

    private fun initButtons(){
        btnGallery  = binding.btnGallery
        btnNetwork  = binding.btnNetwork
        btnGallery.setOnClickListener { startActivity(Intent(this, GalleryActivity::class.java)) }
        btnNetwork.setOnClickListener { startActivity(Intent(this, NetworkActivity::class.java)) }
    }

    private fun initHeadlines(){
        headlines       = binding.headlines

        Thread{
            while(true){
                FetchEmergencyNews(this){ recievedFormattedServerResponse, emergencyInfo->
                    Log.d("FetchEmergencyNews", emergencyInfo)
                    if(!recievedFormattedServerResponse){
                        headlines.text      = emergencyInfo
                        return@FetchEmergencyNews
                    }

                    val allInfo = parseEmergencyInfo(emergencyInfo)

                    var allHeadlines    = ""

                    var headlinesCount  : Int = 0
                    allInfo.description.forEach{ allHeadlines += "Headline ${++headlinesCount}:\n${it}\n" }

                    headlines.text      = allHeadlines.trim()
                }.start()
                sleep(10000)
            }
        }.start()
    }
}