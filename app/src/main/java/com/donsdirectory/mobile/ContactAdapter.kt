package com.donsdirectory.mobile

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

class ContactAdapter(private var contactsList: ArrayList<Contact>?) :
    RecyclerView.Adapter<ContactAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    override fun getItemCount() = contactsList?.size ?: 0

    fun updateList(list: ArrayList<Contact>?) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView = view.findViewById(R.id.contact_name)
        private val contactMobile: TextView = view.findViewById(R.id.contact_mobile)
        private val contactImage: CircleImageView = view.findViewById(R.id.contact_image)

        fun bind(position: Int) {
            val contact = contactsList?.get(position)
            contactName.text = contact?.contactName
            contactMobile.text = contact?.contactNumber

            Glide.with(itemView.context)
                .load(contact?.contactUri)
                .placeholder(R.drawable.ic_account_circle_white)
                .fallback(R.drawable.ic_account_circle_white)
                .error(R.drawable.ic_account_circle_white)
                .into(contactImage)
        }
    }

}