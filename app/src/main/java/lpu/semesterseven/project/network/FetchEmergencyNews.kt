package lpu.semesterseven.project.network

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchEmergencyNews(private val context: Context, private val result: (Boolean, String)->Unit): Thread(), EmergencyInfoService{
    // looper
    private val mainLooper  = Handler(Looper.getMainLooper())

    // activity
    private lateinit var activity   : Activity

    override fun run(){
        try{
            var call    : Call<String> = fetchEmergencyInfo()
            call.enqueue(object: Callback<String>{
                override fun onResponse( call: Call<String?>, response: Response<String?>) {
                    if (response.isSuccessful) {
                        if(response.body()==null) return
                        result(true, response.body()!!)
                    } else{
                        result(false, "Server Error")
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    result(false, "Can't connect to server")
                }

            })
        } catch(e: Exception){
            Log.d("FetchEmergencyNews", "Caught an exception")
            e.printStackTrace()
        }
    }

    override fun fetchEmergencyInfo(): Call<String> = NetworkObjects.emergencyInfoService.fetchEmergencyInfo()

    override fun start(){
        super.start()
        activity    = (context as Activity)
    }
}