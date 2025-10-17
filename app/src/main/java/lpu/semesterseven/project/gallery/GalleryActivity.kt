package lpu.semesterseven.project.gallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import lpu.semesterseven.project.PermissionRequest
import lpu.semesterseven.project.R
import lpu.semesterseven.project.components.toolbar.GalleryToolBar
import lpu.semesterseven.project.databinding.ActivityGalleryBinding
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_NEUTRAL
import lpu.semesterseven.project.dialogs.ACKNOWLEDGEMENT_STATUS_WARNING
import lpu.semesterseven.project.dialogs.promptAcknowledgement
import lpu.semesterseven.project.network.EstablishConnection
import lpu.semesterseven.project.network.NetworkObjects
import lpu.semesterseven.project.network.SendImageService
import lpu.semesterseven.project.permissionCounter
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
    private lateinit var btnSelectImg   : FloatingActionButton
    private lateinit var btnClickImg    : FloatingActionButton
    private lateinit var loadingIndicator   : View

    // state trackers
    private var continueCheckingServerStatus = true
    private lateinit var cameraPermissionRequest: PermissionRequest

    // pick media
    private lateinit var pickMedia      : ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        initImageRecyclerView()

        initButtons()

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

    private fun initButtons(){
        btnSelectImg    = binding.btnSelectImg
        btnClickImg     = binding.btnClickImg
        changeButtonStateTo(false)
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
                changeButtonStateTo(false, true)
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

    private fun changeButtonStateTo(state: Boolean, processingState: Boolean = false){
        if(state){
            btnSelectImg.backgroundTintList = getColorStateList(R.color.primary)
            btnClickImg.backgroundTintList = getColorStateList(R.color.primary)
            btnSelectImg.setOnClickListener{ pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) }
            btnClickImg.setOnClickListener{ clickAnImage() }
            return
        }
        if(processingState){
            btnSelectImg.backgroundTintList = getColorStateList(R.color.primary_light)
            btnClickImg.backgroundTintList = getColorStateList(R.color.primary_light)
            btnSelectImg.setOnClickListener{
                promptAcknowledgement(this, "Cannot perform diagnosis", "Please wait for the current diagnosis to complete", ACKNOWLEDGEMENT_STATUS_NEUTRAL)
            }
            btnClickImg.setOnClickListener{
                promptAcknowledgement(this, "Cannot perform diagnosis", "Please wait for the current diagnosis to complete", ACKNOWLEDGEMENT_STATUS_NEUTRAL)
            }
            return
        }
        btnSelectImg.backgroundTintList = getColorStateList(R.color.status_error)
        btnClickImg.backgroundTintList = getColorStateList(R.color.status_error)
        btnSelectImg.setOnClickListener{
            promptAcknowledgement(this, "Cannot perform diagnosis", "Can't connect to server", ACKNOWLEDGEMENT_STATUS_WARNING)
        }
        btnClickImg.setOnClickListener{
            promptAcknowledgement(this, "Cannot perform diagnosis", "Can't connect to server", ACKNOWLEDGEMENT_STATUS_WARNING)
        }
    }

    private fun changeLoadingVisibilityTo(state: Boolean){
        if(state){
            loadingIndicator.visibility = View.VISIBLE
            return
        }
        loadingIndicator.visibility = View.INVISIBLE
    }

    private fun clickAnImage(){
        Thread{
            cameraPermissionRequest = PermissionRequest(arrayOf(Manifest.permission.CAMERA), ++permissionCounter)

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, cameraPermissionRequest.permissions, cameraPermissionRequest.reqeustCode)
            }
            else{ // when permission was already granted
                cameraPermissionRequest.result = true
                cameraPermissionRequest.latch.countDown()
            }

            cameraPermissionRequest.latch.await()

            if(cameraPermissionRequest.result){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivity(intent)
            }
        }.start()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            cameraPermissionRequest.reqeustCode -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    promptAcknowledgement(this, "Permission was not granted", "Camera permission was not granted", ACKNOWLEDGEMENT_STATUS_WARNING)
                    cameraPermissionRequest.result = false
                }
                else{
                    cameraPermissionRequest.result = true
                }
            }
        }
        cameraPermissionRequest.latch.countDown()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}