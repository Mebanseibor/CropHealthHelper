package lpu.semesterseven.project.gallery

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import lpu.semesterseven.project.R
import lpu.semesterseven.project.components.toolbar.GalleryToolBar
import lpu.semesterseven.project.databinding.ActivityGalleryBinding
import lpu.semesterseven.project.network.EstablishConnection
import lpu.semesterseven.project.network.NetworkObjects
import lpu.semesterseven.project.network.SendImageService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Part
import java.lang.Thread.sleep
import java.util.concurrent.CountDownLatch

class GalleryActivity: AppCompatActivity(), SendImageService{
    // binding
    private lateinit var binding    : ActivityGalleryBinding

    // images
    private lateinit var imagesRV       : RecyclerView
    private lateinit var imagesAdapter  : ImagesAdapter
    private var images                  : MutableList<Image>    = mutableListOf()

    // views
    private lateinit var toolbar        : GalleryToolBar
    private lateinit var btnSelectImg   : ImageButton
    private lateinit var loadingIndicator   : View

    // state trackers
    private var continueCheckingServerStatus = true

    // pick media
    private lateinit var pickMedia      : ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        initImageRecyclerView()

        btnSelectImg    = binding.btnSelectImg
        changeButtonStateTo(false)

        initPickMedia()

        loadingIndicator = binding.loadingIndicator
        performPeriodicNetworkChecks()
    }

    private fun initImageRecyclerView(){
        imagesRV                = binding.images
        imagesAdapter           = ImagesAdapter(this, images)
        imagesRV.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        imagesRV.adapter        = imagesAdapter

        val itemTouchHelper     = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder  : RecyclerView.ViewHolder,
                target      : RecyclerView.ViewHolder
                ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction : Int){
                val position    : Int   = viewHolder.adapterPosition
                val deletedItem : Image = images[position]

                images.removeAt(position)
                imagesAdapter.notifyItemRemoved(position)
                Snackbar.make(imagesRV, "Deleted ${deletedItem.title}", Snackbar.LENGTH_SHORT).setAction("Undo"){
                    images.add(position, deletedItem)
                    imagesAdapter.notifyItemInserted(position)
                }.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(imagesRV)
    }

    private fun initPickMedia(){
        pickMedia = registerForActivityResult(PickVisualMedia()){ uri ->
            if(uri != null){
                Log.d("Gallery", "Selected URI: $uri")
                var mpBody = createMultiPartBody(this, uri)
                if(mpBody == null) return@registerForActivityResult

                var r= uploadImage(mpBody)
                getResponseFromUploadingImage(this, r){ result ->
                    changeLoadingVisibilityTo(false)
                    changeButtonStateTo(true)
                    Log.d("", result)
                    val processedList = parseProcessedImage(result)

                    if(processedList.isEmpty()) return@getResponseFromUploadingImage

                    var firstResult = processedList.first()
                    var image           : Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.parse(uri.toString()))
                    images.add(Image(firstResult.label, image, firstResult.score, firstResult.advice, firstResult.is_healthy))
                    imagesAdapter.notifyItemInserted(images.size - 1)
                }
                changeLoadingVisibilityTo(true)
                changeButtonStateTo(false)
            }
            else{ Log.d("", "No Media was selected (URI was null)") }
        }
    }

    private fun performPeriodicNetworkChecks(){
        Thread{
            while(continueCheckingServerStatus) {
                var checkPerformedLatch: CountDownLatch = CountDownLatch(1)
                EstablishConnection(this) { isServerAlive ->
                    var imgConnectivityStatus: ImageView = binding.connectivityStatus
                    if (isServerAlive) {
                        imgConnectivityStatus.setImageResource(R.drawable.baseline_network_wifi_24)
                        imgConnectivityStatus.imageTintList = getColorStateList(R.color.status_success)
                        changeButtonStateTo(true)
                    } else {
                        imgConnectivityStatus.setImageResource(R.drawable.baseline_signal_wifi_bad_24)
                        imgConnectivityStatus.imageTintList = getColorStateList(R.color.status_error)
                        changeButtonStateTo(false)
                    }
                    checkPerformedLatch.countDown()
                }.start()
                checkPerformedLatch.await()
                sleep(5000)
            }
        }.start()
    }


    private fun changeButtonStateTo(state: Boolean){
        if(state){
            btnSelectImg.setBackgroundColor(resources.getColor(R.color.primary))
            btnSelectImg.setOnClickListener{ pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) }
            return
        }
        btnSelectImg.setBackgroundColor(resources.getColor(R.color.primary_light))
        btnSelectImg.setOnClickListener{}
    }

    private fun changeLoadingVisibilityTo(state: Boolean){
        if(state){
            loadingIndicator.visibility = View.VISIBLE
            return
        }
        loadingIndicator.visibility = View.INVISIBLE
    }

    override fun uploadImage(@Part image: MultipartBody.Part): Call<String> = NetworkObjects.sendImageService.uploadImage(image)

    override fun onDestroy(){
        continueCheckingServerStatus = false
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return toolbar.init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = toolbar.onOptionsItemSelected(item)
}