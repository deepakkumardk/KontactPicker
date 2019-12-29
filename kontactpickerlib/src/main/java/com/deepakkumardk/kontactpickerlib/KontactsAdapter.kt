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
    private var contactsList: MutableList<MyContacts>,
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
        holder.bind(contactsList[position])
    }

    override fun getItemCount(): Int = contactsList.size

    fun updateList(list: MutableList<MyContacts>) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    private fun CircleImageView.loadImage(contactId: String?) {
        Glide.with(this.context)
            .load(getContactImageUri(contactId?.toLong()!!))
            .placeholder(R.drawable.ic_account_circle_white)
            .fallback(R.drawable.ic_account_circle_white)
            .error(R.drawable.ic_account_circle_white)
            .into(this)
    }

    inner class KontactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView = itemView.findViewById(R.id.contact_name)
        private val contactMobile: TextView = itemView.findViewById(R.id.contact_mobile)
        private val contactImage: CircleImageView = itemView.findViewById(R.id.contact_image)
        private val contactTickSmall: CircleImageView = itemView.findViewById(R.id.contact_tick_small)
        private val contactTickLarge: ImageView = itemView.findViewById(R.id.contact_tick_large)

        init {
            itemView.setOnClickListener {
                when (selectionTickView) {
                    SelectionTickView.SmallView -> {
                        listener(contactsList[adapterPosition], adapterPosition, contactTickSmall)
                    }
                    SelectionTickView.LargeView -> {
                        listener(contactsList[adapterPosition], adapterPosition, contactTickSmall)
                    }
                }
            }
        }

        fun bind(contact: MyContacts) {
            contactName.text = contact.contactName
            if (contact.contactNumber?.isNotEmpty() == true)
                contactMobile.text = contact.contactNumber

            when (imageMode) {
                ImageMode.None -> {
                    Glide.with(contactImage.context)
                        .load(R.drawable.ic_account_circle_white)
                        .into(contactImage)
                }
                ImageMode.TextMode -> {
                    contactImage.setImageDrawable(getTextDrawable(contact.contactName ?: ""))
                }
                ImageMode.UserImageMode -> {
                    contactImage.loadImage(contact.contactId)
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