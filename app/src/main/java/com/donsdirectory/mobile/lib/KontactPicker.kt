package com.donsdirectory.mobile.lib

import android.app.Activity
import android.content.Intent
import com.donsdirectory.mobile.activities.KontactPickerActivity
import com.donsdirectory.mobile.model.KontactPickerItem
import com.donsdirectory.mobile.model.MyContacts
import com.donsdirectory.mobile.util.KontactEx
import com.donsdirectory.mobile.util.KontactPickerUI

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactPicker {

    fun startPickerForResult(activity: Activity?, item: KontactPickerItem, requestCode: Int) {
        KontactPickerUI.setPickerUI(item)
        val intent = Intent(activity, KontactPickerActivity::class.java)
        activity.let {
            it?.startActivityForResult(intent, requestCode)
        }
    }

    companion object {
        /**
         * returns selected list of MyContacts
         */
        fun getSelectedKontacts(data: Intent?): ArrayList<MyContacts>? {
            return data?.getParcelableArrayListExtra<MyContacts>(EXTRA_SELECTED_CONTACTS)
        }

        /**
         * returns list of selected contact's phone number
         */
        fun getSelectedPhoneList(data: Intent?): ArrayList<String?> {
            val kontactsList =
                getSelectedKontacts(
                    data
                )
            val phoneList = arrayListOf<String?>()
            kontactsList?.let {
                for (contact in it) {
                    phoneList.add(contact.contactNumber)
                }
            }
            return phoneList
        }

        /**
         * Get All contacts with name and phone number
         */
        fun getAllKontacts(activity: Activity?, onSuccess: (MutableList<MyContacts>) -> Unit) {
            KontactEx().getAllContacts(activity) {
                onSuccess.invoke(it)
            }
        }

        /**
         * Get All contacts with name, phone number and photoUri
         */
        fun getAllKontactsWithUri(
            activity: Activity?,
            getLargeUri: Boolean = false,
            onSuccess: (MutableList<MyContacts>) -> Unit
        ) {
            val item = KontactPickerItem().apply {
                includePhotoUri = true
                getLargePhotoUri = getLargeUri
            }
            KontactPickerUI.setPickerUI(item)
            KontactEx().getAllContacts(activity) {
                onSuccess.invoke(it)
            }
        }
    }

}