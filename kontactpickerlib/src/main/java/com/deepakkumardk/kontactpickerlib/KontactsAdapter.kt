package com.deepakkumardk.kontactpickerlib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deepakkumardk.kontactpickerlib.model.MyContacts
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.find

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactsAdapter(
    private var contactsList: MutableList<MyContacts>?,
    private val selectionTickView: Int,
    private val imageMode: Int,
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

        when (imageMode) {
            0 -> Glide.with(holder.itemView.context)
                .load(R.drawable.ic_account_circle_white)
                .into(holder.contactImage)
            1 -> holder.contactImage.setImageDrawable(
                getTextDrawable(
                    contact.contactName!!
                )
            )
        }

        holder.itemView.setOnClickListener {
            if (selectionTickView == 0)
                listener(contact, holder.adapterPosition, holder.contactTickSmall)
            else
                listener(contact, holder.adapterPosition, holder.contactTickLarge)
        }

        when (contact.isSelected) {
            true -> {
                if (selectionTickView == 0)
                    holder.contactTickSmall.show()
                else
                    holder.contactTickLarge.show()
            }
            false -> {
                if (selectionTickView == 0)
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
        val contactTickLarge = view.find<ImageView>(R.id.contact_tick_large)
    }

}