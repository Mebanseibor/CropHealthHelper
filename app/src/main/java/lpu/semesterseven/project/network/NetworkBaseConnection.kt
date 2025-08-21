package lpu.semesterseven.project.network

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

abstract class NetworkBaseConnection(protected open var context: Context): Thread(),  ServerAPIService {
    // main looper
    protected var mainLooper  = Handler(Looper.getMainLooper())

    // activity
    protected lateinit var activity: Activity

    override fun start(){
        super.start()
        activity    = (context as Activity)
    }

    override fun interrupt() {
        super.interrupt()
        Log.d("Interrupt", "Interrupted")
    }
}