package com.deepakkumardk.kontactpicker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deepakkumardk.kontactpicker.model.MyContacts
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactsAdapter(
    private var contactsList: MutableList<MyContacts>?,
    private val smallView: Boolean,
    private val listener: (MyContacts, Int, View) -> Unit
) : RecyclerView.Adapter<KontactsAdapter.KontactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontactViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kontact, parent, false)
        return KontactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KontactViewHolder, position: Int) {
        val contact = contactsList!![position] as MyContacts?
        holder.contactName.text = contact?.contactName
        if (contact?.contactNumber?.isNotEmpty()!!)
            holder.contactMobile.text = contact.contactNumber
//        (Optimize this)
        /*Glide.with(holder.itemView.context)
            .load(contactsList[position].contactImageByte)
            .asBitmap()
            .placeholder(R.drawable.ic_account_circle_white)
            .error(R.drawable.ic_account_circle_white)
            .into(holder.contactImage)*/

        holder.itemView.setOnClickListener {
            if (smallView)
                listener(contact, holder.adapterPosition, holder.contactTickSmall)
            else
                listener(contact, holder.adapterPosition, holder.contactTickLarge)
        }

        when (contact.isSelected) {
            true -> {
                if (smallView)
                    holder.contactTickSmall.show()
                else
                    holder.contactTickLarge.show()
            }
            false -> {
                if (smallView)
                    holder.contactTickSmall.hide()
                else
                    holder.contactTickLarge.hide()

            }
        }
    }

    override fun getItemCount(): Int = contactsList?.size!!

    fun updateList(list: MutableList<MyContacts>) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    class KontactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName = view.find<TextView>(R.id.contact_name)
        val contactMobile = view.find<TextView>(R.id.contact_mobile)
        val contactImage = view.find<CircleImageView>(R.id.contact_image)
        val contactTickSmall = view.find<CircleImageView>(R.id.contact_tick_small)
        val contactTickLarge = view.find<CircleImageView>(R.id.contact_tick_large)
    }

}