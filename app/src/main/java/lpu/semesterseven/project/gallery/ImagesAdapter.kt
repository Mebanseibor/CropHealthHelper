package lpu.semesterseven.project.gallery

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.R

class ImagesAdapter(private var context: Context, private var images: List<Image>): RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>(){
    inner class ImagesViewHolder(var itemView: View): RecyclerView.ViewHolder(itemView){
        var image               : ImageView = itemView.findViewById(R.id.image)
        var title               : TextView  = itemView.findViewById(R.id.title)
        var cropStateIndicator  : ImageView = itemView.findViewById(R.id.cropStateIndicator)
        var score               : TextView  = itemView.findViewById(R.id.score)
        var advice              : TextView  = itemView.findViewById(R.id.advice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder{
        val view    : View = LayoutInflater.from(context).inflate(R.layout.list_item_gallery, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int){
        var image: Image                = images[position]

        holder.title.text               = image.title
        holder.score.text               = "Confidence level: ${(image.score*100.00F).toString().substringBeforeLast('.')}%"
        holder.score.visibility         = View.GONE
        holder.advice.text              = "Advice: ${image.advice}"
        var scorePercentageValue: Int    = (image.score*100).toInt()

        if (image.image!=null) holder.image.setImageBitmap(image.image)
        else holder.image.setImageResource(image.imageDrawable)

        if(!image.is_healthy) {
            holder.cropStateIndicator.setImageResource(R.drawable.baseline_sick_24)
            val statusIndicatorColor = ContextCompat.getColor(context, R.color.status_error)

            ImageViewCompat.setImageTintList(
                holder.cropStateIndicator,
                ColorStateList.valueOf(statusIndicatorColor)
            )
        }

        var color: Int
        if      (scorePercentageValue>=85)  color = R.color.confidence_vhigh
        else if (scorePercentageValue>=75)  color = R.color.confidence_high
        else if (scorePercentageValue>=40)  color = R.color.confidence_medium
        else if (scorePercentageValue>=25)  color = R.color.confidence_low
        else                                color = R.color.confidence_vlow

        holder.score.setTextColor(context.getResources().getColor(color))

        holder.itemView.setOnClickListener{
            Toast.makeText(context, "Selected ${image.title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = images.size
}