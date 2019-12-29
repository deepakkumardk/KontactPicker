package com.deepakkumardk.kontactpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class ContactAdapter(private var contactsList: ArrayList<Contact>) :
    RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(contactsList[position])
    }

    override fun getItemCount(): Int = contactsList.size

    fun updateList(list: ArrayList<Contact>) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView = itemView.findViewById(R.id.contact_name)
        private val contactMobile: TextView = itemView.findViewById(R.id.contact_mobile)
        private val contactImage: CircleImageView = itemView.findViewById(R.id.contact_image)

        fun bind(contact: Contact) {
            contactName.text = contact.contactName
            contactMobile.text = contact.contactNumber

            Glide.with(itemView.context)
                .load(contact.contactUri)
                .placeholder(R.drawable.ic_account_circle_white)
                .fallback(R.drawable.ic_account_circle_white)
                .error(R.drawable.ic_account_circle_white)
                .into(contactImage)
        }
    }

}