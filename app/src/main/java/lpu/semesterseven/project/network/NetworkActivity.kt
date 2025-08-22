package lpu.semesterseven.project.network

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityNetworkBinding

class NetworkActivity: AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityNetworkBinding

    // views
    private lateinit var resultText : TextView
    private lateinit var resultImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_network)

        initViews()
        EstablishConnection(this, true){ isServerAlive->
            Log.d("", "isServerAlive: $isServerAlive")
        }.start()
    }

    private fun initViews(){
        resultText  = binding.result
        resultImage = binding.resultImage
    }
}