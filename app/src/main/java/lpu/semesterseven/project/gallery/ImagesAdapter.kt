package lpu.semesterseven.project.gallery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.R

class ImagesAdapter(private var context: Context, private var images: List<Image>): RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>(){
    inner class ImagesViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView){
        var image   : ImageView = itemView.findViewById(R.id.image)
        var title   : TextView  = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder{
        val view    : View = LayoutInflater.from(context).inflate(R.layout.list_item_gallery, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int){
        var image: Image    = images[position]

        holder.title.text   = image.title
        holder.image.setImageResource(image.image)

        holder.itemView.setOnClickListener{
            Toast.makeText(context, "Selected ${image.title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = images.size
}