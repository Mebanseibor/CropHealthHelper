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

        fun setData(imageItem: Image){
            title.text               = imageItem.title
            score.text               = "Confidence level: ${(imageItem.score*100.00F).toString().substringBeforeLast('.')}%"
            score.visibility         = View.GONE
            advice.text              = "Advice: ${imageItem.advice}"
            var scorePercentageValue: Int    = (imageItem.score*100).toInt()

            if (imageItem.image!=null) image.setImageBitmap(imageItem.image)
            else image.setImageResource(imageItem.imageDrawable)

            if(!imageItem.is_healthy) {
                cropStateIndicator.setImageResource(R.drawable.baseline_sick_24)
                val statusIndicatorColor = ContextCompat.getColor(context, R.color.status_error)

                ImageViewCompat.setImageTintList(
                    cropStateIndicator,
                    ColorStateList.valueOf(statusIndicatorColor)
                )
            }

            var color: Int
            if      (scorePercentageValue>=85)  color = R.color.confidence_vhigh
            else if (scorePercentageValue>=75)  color = R.color.confidence_high
            else if (scorePercentageValue>=40)  color = R.color.confidence_medium
            else if (scorePercentageValue>=25)  color = R.color.confidence_low
            else                                color = R.color.confidence_vlow

            score.setTextColor(context.getResources().getColor(color))

            itemView.setOnClickListener{
                Toast.makeText(context, "Selected ${imageItem.title}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder{
        val view    : View = LayoutInflater.from(context).inflate(R.layout.list_item_gallery, parent, false)
        return ImagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int){
        var imageItem: Image                = images[position]

        holder.setData(imageItem)

        // saving the file
        // var file = AnalysedImageJSONFileHandler(context, assignRandomFileName(), imageItem)
        // file.write()
    }

    override fun getItemCount(): Int = images.size
}