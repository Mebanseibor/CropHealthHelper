package lpu.semesterseven.project.gallery

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityGalleryBinding
import lpu.semesterseven.project.network.NetworkObjects
import lpu.semesterseven.project.network.SendImageService
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Part

class GalleryActivity: AppCompatActivity(), SendImageService{
    // binding
    private lateinit var binding    : ActivityGalleryBinding

    // images
    private lateinit var imagesRV       : RecyclerView
    private lateinit var imagesAdapter  : ImagesAdapter
    private var images                  : MutableList<Image>    = mutableListOf()

    // views
    private lateinit var btnSelectImg   : ImageButton

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        initImages()
        initSelectImage()
    }

    private fun initImages(){
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

    private fun initSelectImage(){
        btnSelectImg    = binding.btnSelectImg

        val pickMedia = registerForActivityResult(PickVisualMedia()){ uri ->
            if(uri != null){
                Log.d("Gallery", "Selected URI: $uri")
                var mpBody = createMultiPartBody(this, uri)
                if(mpBody == null) return@registerForActivityResult

                var r= uploadImage(mpBody)
                getResponseFromUploadingImage(this, r){ result ->
                    Log.d("", result)
                    val processedList = parseJsonToDataClass(result)

                    if(processedList.isEmpty()) return@getResponseFromUploadingImage

                    var firstResult = processedList.first()
                    images.add(Image(firstResult.label, firstResult.score))
                    imagesAdapter       = ImagesAdapter(this, images)
                    imagesRV.adapter    = imagesAdapter
                }
            }
            else{ Log.d("", "No Media was selected (URI was null)") }
        }

        btnSelectImg.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    override fun uploadImage(@Part image: MultipartBody.Part): Call<String> = NetworkObjects.sendImageService.uploadImage(image)
}