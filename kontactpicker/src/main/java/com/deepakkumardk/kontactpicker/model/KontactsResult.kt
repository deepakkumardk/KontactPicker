package com.deepakkumardk.kontactpicker.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KontactsResult(
    var contactName: String? = null,
    var contactNumber: String? = null,
    var isSelected: Boolean = false,
    var contactNumberList: ArrayList<String> = arrayListOf()
) : Parcelable