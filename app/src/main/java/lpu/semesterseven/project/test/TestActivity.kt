package lpu.semesterseven.project.test

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityTestBinding

class TestActivity: AppCompatActivity(){
    // binding
    private lateinit var binding: ActivityTestBinding

    // views
    private lateinit var btnFetch   : Button
    private lateinit var imgView    : ImageView

    private val testViewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        initViewModel()
    }

    private fun initViewModel(){
        btnFetch    = binding.btnFetch
        imgView     = binding.image

        testViewModel.imageUrl.observe(this){ url->
            Glide.with(this)
                .load(url)
                .skipMemoryCache(true)
                .into(imgView)
        }

        btnFetch.setOnClickListener {
            testViewModel.startLiveImages()
        }
    }
}