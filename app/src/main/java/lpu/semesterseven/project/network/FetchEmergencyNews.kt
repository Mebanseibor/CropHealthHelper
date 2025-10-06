package lpu.semesterseven.project.network

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchEmergencyNews(private val context: Context, private val result: (Boolean, String, String)->Unit): Thread(), EmergencyInfoService{
    // looper
    private val mainLooper  = Handler(Looper.getMainLooper())

    // activity
    private lateinit var activity   : Activity

    // messages
    private var news    = "Unknown error while fetching emergency news"
    private var errMsg  = ""

    override fun run(){
        try{
            var call    : Call<String> = fetchEmergencyInfo()
            call.enqueue(object: Callback<String>{
                override fun onResponse( call: Call<String?>, response: Response<String?>) {
                    if (response.isSuccessful) {
                        if (response.body() == null){
                            result(false, news, "Server error: (No response body received")
                            return
                        }
                        result(true, response.body()!!, errMsg)
                    } else{
                        result(false, news, "Server Error: (Server may not be alive)")
                    }
                }

                override fun onFailure(call: Call<String?>, t: Throwable) {
                    result(false, news, "Can't connect to server: (Your internet connectivity may be off)")
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