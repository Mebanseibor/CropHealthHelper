package lpu.semesterseven.project.gallery

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import lpu.semesterseven.project.R
import lpu.semesterseven.project.databinding.ActivityGalleryBinding

class GalleryActivity: AppCompatActivity() {
    // binding
    private lateinit var binding    : ActivityGalleryBinding

    // images
    private lateinit var imagesRV   : RecyclerView
    private lateinit var imagesAdapter  : ImagesAdapter
    private var images  = mutableListOf(
        Image("Apple"),
        Image("Banana"),
        Image("Litchi", R.drawable.baseline_check_24)
    )

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        initImages()
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

                Snackbar.make(imagesRV, "Delete ${deletedItem.title}?", Snackbar.LENGTH_INDEFINITE).setAction("Undo"){
                    images.add(position, deletedItem)
                    imagesAdapter.notifyItemInserted(position)
                }.show()
            }
        })

        itemTouchHelper.attachToRecyclerView(imagesRV)
    }
}