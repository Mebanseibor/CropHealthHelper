package lpu.semesterseven.project.serverinfo.headlines

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.R
import lpu.semesterseven.project.serverinfo.Info
import lpu.semesterseven.project.serverinfo.SERVER_INFO_STATE_CRITICAL
import lpu.semesterseven.project.serverinfo.SERVER_INFO_STATE_GOOD
import lpu.semesterseven.project.serverinfo.SERVER_INFO_STATE_IMPORTANT
import lpu.semesterseven.project.serverinfo.SERVER_INFO_STATE_NORMAL
import lpu.semesterseven.project.serverinfo.SERVER_INFO_STATE_WARNING
import lpu.semesterseven.project.serverinfo.infoIcon
import lpu.semesterseven.project.serverinfo.infoIconDefault

class HeadlinesAdapter(private var context: Context, private var headlines: List<Info>): RecyclerView.Adapter<HeadlinesAdapter.HeadlinesViewHolder>(){
    class HeadlinesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var icon        : ImageView = itemView.findViewById<ImageView>(R.id.icon)
        var headline    : TextView  = itemView.findViewById<TextView>(R.id.headline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeadlinesViewHolder {
        var view    : View = LayoutInflater.from(context).inflate(R.layout.list_item_healine, parent, false)
        return HeadlinesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: HeadlinesViewHolder,
        position: Int
    ) {
        var item    : Info = headlines[position]

        holder.icon.setImageResource(infoIcon.getOrDefault(item.icon, infoIconDefault))
        holder.headline.text   = item.description

        var textColor       : Int = when(item.level){
            SERVER_INFO_STATE_CRITICAL  -> R.color.info_critical
            SERVER_INFO_STATE_IMPORTANT -> R.color.info_important
            SERVER_INFO_STATE_WARNING   -> R.color.info_warning
            SERVER_INFO_STATE_NORMAL    -> R.color.info_normal
            SERVER_INFO_STATE_GOOD      -> R.color.info_good
            else                        -> R.color.info_neutral
        }
        holder.headline.setTextColor(context.getColor(textColor))
    }

    override fun getItemCount(): Int = headlines.size

    fun updateData(newHeadlines: List<Info>) {
        this.headlines = newHeadlines
        notifyDataSetChanged()
    }
}