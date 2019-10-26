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
        val contact = contactsList!![position] as MyContacts?
        holder.contactName.text = contact?.contactName
        if (contact?.contactNumber?.isNotEmpty()!!)
            holder.contactMobile.text = contact.contactNumber

        when (imageMode) {
            ImageMode.None -> {
                Glide.with(holder.itemView.context)
                    .load(R.drawable.ic_account_circle_white)
                    .into(holder.contactImage)
            }
            ImageMode.TextMode -> {

                holder.contactImage.setImageDrawable(
                    getTextDrawable(contact.contactName!!)
                )
            }
            ImageMode.UserImageMode -> {
                Glide.with(holder.itemView.context)
                    .load(getContactImageUri(contact.contactId?.toLong()!!))
                    .placeholder(R.drawable.ic_account_circle_white)
                    .fallback(R.drawable.ic_account_circle_white)
                    .error(R.drawable.ic_account_circle_white)
                    .into(holder.contactImage)
            }
        }

        holder.itemView.setOnClickListener {
            when (selectionTickView) {
                SelectionTickView.SmallView -> listener(
                    contact,
                    holder.adapterPosition,
                    holder.contactTickSmall
                )
                else -> listener(contact, holder.adapterPosition, holder.contactTickLarge)
            }
        }

        when (contact.isSelected) {
            true -> {
                when (selectionTickView) {
                    SelectionTickView.SmallView -> holder.contactTickSmall.show()
                    SelectionTickView.LargeView -> holder.contactTickLarge.show()
                }
            }
            false -> {
                when (selectionTickView) {
                    SelectionTickView.SmallView -> holder.contactTickSmall.hide()
                    SelectionTickView.LargeView -> holder.contactTickLarge.hide()
                }
            }
        }
    }

    override fun getItemCount(): Int = contactsList?.size!!

    fun updateList(list: MutableList<MyContacts>) {
        this.contactsList = list
        notifyDataSetChanged()
    }

    class KontactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contactName: TextView = view.findViewById(R.id.contact_name)
        val contactMobile: TextView = view.findViewById(R.id.contact_mobile)
        val contactImage: CircleImageView = view.findViewById(R.id.contact_image)
        val contactTickSmall: CircleImageView = view.findViewById(R.id.contact_tick_small)
        val contactTickLarge: ImageView = view.findViewById(R.id.contact_tick_large)
    }

}