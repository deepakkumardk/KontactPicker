package com.deepakkumardk.kontactpickerlib.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Deepak Kumar on 25/05/2019
 */

@Parcelize
data class MyContacts(
    var contactId: String? = null,
    var contactName: String? = null,
    var contactNumber: String? = null,
    var isSelected: Boolean = false,
    var photoUri: Uri? = null,
    var contactNumberList: ArrayList<String> = arrayListOf()
) : Parcelable