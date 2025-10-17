package lpu.semesterseven.project.network

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityNetworkBinding
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_FAILURE
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_SUCCESS
import lpu.semesterseven.project.dialogs.promptAcknowledgement

class NetworkActivity: AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityNetworkBinding

    // views
    private lateinit var resultText : TextView
    private lateinit var resultImage: ImageView

    // state trackers
    private var checkServerStatus   : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_network)

        initViews()
        EstablishConnection(this, true){ isServerAlive ->
            if(!checkServerStatus) return@EstablishConnection
            runOnUiThread { displayServerState(isServerAlive) }
        }.start()
    }

    private fun initViews(){
        resultText  = binding.result
        resultImage = binding.resultImage
    }

    private fun displayServerState(state: Boolean){
        if(state) promptAcknowledgement(this, "Connected to Server", "", ACKNOWLEDGEMENT_STATUS_SUCCESS)
        else promptAcknowledgement(this, "Cannot Connect to Server", "", ACKNOWLEDGEMENT_STATUS_FAILURE)
    }

    override fun onDestroy(){
        super.onDestroy()
        checkServerStatus = false
    }
}