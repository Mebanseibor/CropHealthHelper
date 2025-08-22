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
        var score   : TextView  = itemView.findViewById(R.id.score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder{
        val view    : View = LayoutInflater.from(context).inflate(R.layout.list_item_gallery, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int){
        var image: Image                = images[position]

        holder.title.text               = image.title
        holder.score.text               = (image.score*100.00F).toString().substringBeforeLast('.')+"%"
        var scorePercenageValue: Int    = (image.score*100).toInt()
        holder.image.setImageResource(image.image)

        var color: Int
        if      (scorePercenageValue>=85)  color = R.color.color_success_green
        else if (scorePercenageValue>=75)  color = R.color.color_warning_yellow
        else if (scorePercenageValue>=40)  color = R.color.color_alert_orange
        else                        color = R.color.color_error_red

        holder.score.setTextColor(context.getResources().getColor(color))

        holder.itemView.setOnClickListener{
            Toast.makeText(context, "Selected ${image.title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = images.size
}