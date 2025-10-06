package lpu.semesterseven.project.testmedia

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import lpu.semesterseven.project.R

class MusicService: Service() {
    // media objects
    private var mp      : MediaPlayer? = null
    private var binder  = LocalBinder()

    inner class LocalBinder: Binder(){
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        if(mp!=null) return binder

        mp = MediaPlayer.create(this, R.raw.sample_alert_audio).apply{
            isLooping = true
        }
        return binder
    }

    fun play()  { mp?.start() }
    fun stop()  { mp?.stop() }

    override fun onDestroy(){
        mp?.release()
        mp = null
        super.onDestroy()
    }
}