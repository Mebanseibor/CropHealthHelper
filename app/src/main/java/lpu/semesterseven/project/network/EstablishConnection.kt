package lpu.semesterseven.project.network

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import lpu.semesterseven.project.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class EstablishConnection(override var context: Context): NetworkBaseConnection(context){
    // views
    private lateinit var resultText     : TextView
    private lateinit var resultImage    : ImageView
    private lateinit var progressBar    : ProgressBar

    private var connectionAttemptLatch  : CountDownLatch = CountDownLatch(1)

    // state tracker
    private var isConnectionEstablished : Boolean = false

    override fun run(){
        try{
            if(!attemptConnectionToServer()){
                showError("Cannot connect to server")
                return
            }
        } catch(e: Exception){
            Log.d("EstablishConnection", "Encountered an error")
            e.printStackTrace()
        }
    }

    override fun start(){
        super.start()
        resultText  = activity.findViewById<TextView>(R.id.result)
        resultImage = activity.findViewById<ImageView>(R.id.resultImage)
        progressBar = activity.findViewById<ProgressBar>(R.id.progressBar)
    }

    private fun attemptConnectionToServer(): Boolean{
        showLoading()

        var call = getResult()
        call.enqueue(object: Callback<String>{
            override fun onResponse( call: Call<String?>, response: Response<String?> ){
                if (response.isSuccessful) {
                    var str = response.body()?:"NULL"
                    showResult(str)

                    isConnectionEstablished = true
                } else {
                    showError("Server Error")
                    isConnectionEstablished = false
                }
                connectionAttemptLatch.countDown()
            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                showError("Can't connect to server")
                isConnectionEstablished = false
            }
        })
        connectionAttemptLatch.await()

        return isConnectionEstablished
    }

    private fun showSuccess(){
        mainLooper.post{
            resultImage.setImageResource(R.drawable.baseline_check_24)
            resultImage.visibility  = View.VISIBLE

            progressBar.visibility  = View.GONE
        }
    }

    private fun showError(errMsg: String){
        mainLooper.post{
            resultText.text = errMsg

            resultImage.setImageResource(R.drawable.baseline_error)
            resultImage.visibility  = View.VISIBLE

            progressBar.visibility  = View.GONE
        }
    }

    private fun showLoading(){
        for(i in 0..24){
            sleep(200)
            if(i%6==0) {
                mainLooper.post { resultText.text = "Establishing mock connection" }
            }
            else{
                mainLooper.post{ resultText.text = resultText.text.toString() + "." }
            }
        }
    }

    private fun showResult(resultMsg: String){
        mainLooper.post {
            resultText.text = resultMsg
            showSuccess()
        }
    }

    override fun getResult(): Call<String> = NetworkObjects.diseaseAPIService.getResult()
}