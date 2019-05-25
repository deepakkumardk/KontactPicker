package com.deepakkumardk.kontactpicker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyContacts(
    var contactName: String? = null,
    var contactNumber: String? = null,
    var isSelected: Boolean = false,
    var contactNumberList: ArrayList<String> = arrayListOf()
) : Parcelable