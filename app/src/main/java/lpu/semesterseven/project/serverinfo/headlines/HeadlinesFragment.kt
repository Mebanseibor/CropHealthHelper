package lpu.semesterseven.project.serverinfo.headlines

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.databinding.FragmentHeadlinesBinding
import lpu.semesterseven.project.network.FetchEmergencyNews
import lpu.semesterseven.project.network.NetworkObjects
import lpu.semesterseven.project.serverinfo.Info
import lpu.semesterseven.project.serverinfo.parseInfo
import java.lang.Thread.sleep

class HeadlinesFragment: Fragment() {
    // binding
    private var _binding    : FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!

    // views
    private lateinit var headlinesMsg: TextView

    // recycler
    private lateinit var rv         : RecyclerView
    private lateinit var adapter    : HeadlinesAdapter
    private var headlines           : List<Info> = listOf()

    // state trackers
    private var continueCheckingServerStatus = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        initHeadlines()
        return binding.root
    }

    private fun initHeadlines(){
        headlinesMsg    = binding.headlinesMsg
        rv              = binding.headlinesRV
        adapter         = HeadlinesAdapter(requireContext(), headlines)
        rv.layoutManager= LinearLayoutManager(requireContext())
        rv.adapter      = adapter
        Log.d("initHeadlines", "Establishing connection to base url ${NetworkObjects.retrofit.baseUrl()}")

        Thread{
            while(continueCheckingServerStatus){
                FetchEmergencyNews(requireContext()){ receivedFormattedServerResponse, headlines, errMsg->
                    Log.d("FetchEmergencyNews", headlines)
                    if(!receivedFormattedServerResponse){
                        displayMsg(errMsg)
                        return@FetchEmergencyNews
                    }

                    this.headlines = parseInfo(headlines)

                    if(headlines.isEmpty()){
                        displayMsg("No headlines right now")
                        return@FetchEmergencyNews
                    }

                    Handler(Looper.getMainLooper()).post{
                        headlinesMsg.visibility = View.GONE
                        rv.visibility           = View.VISIBLE
                    }
                    adapter.updateData(this.headlines)
                }.start()
                sleep(5000)
            }
        }.apply{start()}
    }

    private fun displayMsg(msg: String){
        Log.d("FetchEmergencyNews", "displayMsg: $msg")
        rv.visibility = View.GONE
        headlinesMsg.text = msg
        headlinesMsg.visibility = View.VISIBLE
    }

    override fun onDestroy(){
        continueCheckingServerStatus = false
        super.onDestroy()
        _binding = null
    }
}