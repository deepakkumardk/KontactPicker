package com.deepakkumardk.kontactpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class ContactAdapter(private var contactsList: ArrayList<Contact>?) :
    RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact = contactsList!![position] as Contact?
        holder.contactName.text = contact?.contactName
        holder.contactMobile.text = contact?.contactNumber
    }

    override fun getItemCount(): Int = contactsList?.size!!

    fun updateList(list: ArrayList<Contact>?) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: TextView = view.findViewById(R.id.contact_name)
        val contactMobile: TextView = view.findViewById(R.id.contact_mobile)
        val contactImage: CircleImageView = view.findViewById(R.id.contact_image)
    }

}