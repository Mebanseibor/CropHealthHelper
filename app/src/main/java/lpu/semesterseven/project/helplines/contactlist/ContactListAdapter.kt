package lpu.semesterseven.project.helplines.contactlist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lpu.semesterseven.project.R
import androidx.core.net.toUri

class ContactListAdapter(private var context: Context, private var contacts: ArrayList<Contact>): RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>(){
    inner class ContactViewHolder(view: View): RecyclerView.ViewHolder(view){
        var name    : TextView  = view.findViewById(R.id.name)
        var number  : TextView  = view.findViewById(R.id.number)
        var btnCall : ImageButton = view.findViewById(R.id.btnCall)

        fun CallNumber(number: Long){
            var intent = Intent(Intent.ACTION_DIAL).apply{
                data = "tel:$number".toUri()
            }
            (context as Activity).startActivity(intent)
        }
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): ContactViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder( holder: ContactViewHolder, position: Int ) {
        var contact = contacts[position]

        holder.name.text    = contact.name
        holder.number.text  = "Number: ${contact.number}"

        holder.btnCall.setOnClickListener{ holder.CallNumber(contact.number) }
    }

    override fun getItemCount(): Int = contacts.size
}