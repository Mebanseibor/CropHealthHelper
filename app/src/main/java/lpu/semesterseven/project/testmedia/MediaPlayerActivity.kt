package lpu.semesterseven.project.testmedia

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityMediaplayerBinding

class MediaPlayerActivity: AppCompatActivity() {
    // binding
    private lateinit var binding : ActivityMediaplayerBinding

    // buttons
    private lateinit var btnBind    : ImageButton
    private lateinit var btnUnbind  : ImageButton
    private lateinit var btnPlay    : ImageButton
    private lateinit var btnStop    : ImageButton

    // music service
    private var isMBounded  = false
    private var mService    : MusicService? = null

    private val connection = createConnection()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mediaplayer)

        initButtons()
    }

    private fun initButtons(){
        btnBind     = binding.btnBind
        btnUnbind   = binding.btnUnbind
        btnPlay     = binding.btnPlay
        btnStop     = binding.btnStop

        btnBind.setOnClickListener {
            if(isMBounded){
                Toast.makeText(this, "Music Service is already Bounded", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Attempting to bind service", Toast.LENGTH_SHORT).show()
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        btnUnbind.setOnClickListener {
            if(!isMBounded){
                Toast.makeText(this, "Music Service is not yet Bounded", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            unbindService(connection)
            isMBounded = false
            mService?.onDestroy()
        }
        btnPlay.setOnClickListener {
            if(!isMBounded){
                Toast.makeText(this, "Music Service is not Bounded", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mService?.play()
        }
        btnStop.setOnClickListener {
            if(!isMBounded){
                Toast.makeText(this, "Music Service is not Bounded", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mService?.stop()
        }
    }

    private fun createConnection(): ServiceConnection{
        return object: ServiceConnection{
            override fun onServiceConnected( name: ComponentName?, service: IBinder? ) {
                val binder = service as MusicService.LocalBinder
                mService = binder.getService()
                isMBounded = true
                Toast.makeText(this@MediaPlayerActivity, "Service is bounded", Toast.LENGTH_SHORT).show()
            }

            override fun onServiceDisconnected(name: ComponentName?) { isMBounded = false }
        }
    }
}