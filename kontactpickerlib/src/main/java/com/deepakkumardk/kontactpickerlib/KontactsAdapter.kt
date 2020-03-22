package com.deepakkumardk.kontactpickerlib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deepakkumardk.kontactpickerlib.model.ImageMode
import com.deepakkumardk.kontactpickerlib.model.MyContacts
import com.deepakkumardk.kontactpickerlib.model.SelectionTickView
import com.deepakkumardk.kontactpickerlib.util.*
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactsAdapter(
    private var contactsList: MutableList<MyContacts>?,
    private val listener: (MyContacts, Int, View) -> Unit
) : RecyclerView.Adapter<KontactsAdapter.KontactViewHolder>() {

    private var imageMode: ImageMode = KontactPickerUI.getImageMode()
    private var selectionTickView: SelectionTickView = KontactPickerUI.getSelectionTickView()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontactViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kontact, parent, false)
        return KontactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: KontactViewHolder, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    override fun getItemCount() = contactsList?.size ?: 0

    fun updateList(list: MutableList<MyContacts>) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    inner class KontactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contactName: TextView = view.findViewById(R.id.contact_name)
        private val contactMobile: TextView = view.findViewById(R.id.contact_mobile)
        private val contactImage: CircleImageView = view.findViewById(R.id.contact_image)
        private val contactTickSmall: CircleImageView = view.findViewById(R.id.contact_tick_small)
        private val contactTickLarge: ImageView = view.findViewById(R.id.contact_tick_large)

        init {
            itemView.setOnClickListener {
                val contact = contactsList?.get(adapterPosition)
                if (contact != null)
                    when (selectionTickView) {
                        SelectionTickView.SmallView -> listener(
                            contact, adapterPosition, contactTickSmall
                        )
                        else -> listener(contact, adapterPosition, contactTickLarge)
                    }
            }
        }

        fun bind(position: Int) {
            val contact = contactsList?.get(position)
            contactName.text = contact?.contactName
            if (contact?.contactNumber?.isNotEmpty()!!)
                contactMobile.text = contact.contactNumber

            when (imageMode) {
                ImageMode.None -> {
                    Glide.with(itemView.context)
                        .load(R.drawable.ic_account_circle_white)
                        .into(contactImage)
                }
                ImageMode.TextMode -> {
                    contactImage.setImageDrawable(getTextDrawable(contact.contactName ?: ""))
                }
                ImageMode.UserImageMode -> {
                    Glide.with(itemView.context)
                        .load(getContactImageUri(contact.contactId?.toLong() ?: 0))
                        .placeholder(R.drawable.ic_account_circle_white)
                        .fallback(R.drawable.ic_account_circle_white)
                        .error(R.drawable.ic_account_circle_white)
                        .into(contactImage)
                }
            }

            when (contact.isSelected) {
                true -> {
                    when (selectionTickView) {
                        SelectionTickView.SmallView -> contactTickSmall.show()
                        SelectionTickView.LargeView -> contactTickLarge.show()
                    }
                }
                false -> {
                    when (selectionTickView) {
                        SelectionTickView.SmallView -> contactTickSmall.hide()
                        SelectionTickView.LargeView -> contactTickLarge.hide()
                    }
                }
            }
        }
    }

}