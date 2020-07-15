package com.donsdirectory.mobile.model

import android.net.Uri

/**
 * Created by Deepak Kumar on 25/05/2019
 */

data class Contact(
    var contactName: String? = null,
    var contactNumber: String? = null,
    var contactUri: Uri? = null
)