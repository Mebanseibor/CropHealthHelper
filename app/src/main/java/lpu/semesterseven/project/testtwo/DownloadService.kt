package lpu.semesterseven.project.testtwo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DownloadService: Service() {
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val downloading_msg = "Downloading started"
        Toast.makeText(this, downloading_msg, Toast.LENGTH_SHORT).show()
        Log.d("Debugging", downloading_msg)

        serviceScope.launch{
            for(i in 1 ..5){
                delay(1000)
                Log.d("Debugging", "Downloading file...")
            }
            Log.d("Debugging", "Downloading finished")
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy(){
        super.onDestroy()
        val downloading_stopped_msg = "Downloading service stopped"
        Toast.makeText(this, downloading_stopped_msg, Toast.LENGTH_SHORT).show()
        serviceScope.cancel()
        Log.d("Debugging", downloading_stopped_msg)
    }
}