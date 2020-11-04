package com.donsdirectory.mobile.util

import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.donsdirectory.mobile.model.MyContacts
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete

/**
 * Created by Deepak Kumar on 23/08/2019
 */

class KontactEx {

    fun getAllContacts(activity: Activity?, onCompleted: (MutableList<MyContacts>) -> Unit) {
        val startTime = System.currentTimeMillis()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Email.ADDRESS
        )

        val contactMap = mutableMapOf<String, MyContacts>()
        val cr = activity?.contentResolver
        doAsyncResult {
            cr?.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
                null, null, null
            )?.use {
                val idIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
                val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val emailIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)

                var id: String
                var name: String
                var number: String
                var email: String
                while (it.moveToNext()) {
                    val contacts = MyContacts()
                    id = it.getLong(idIndex).toString()
                    name = it.getString(nameIndex)
                    number = it.getString(numberIndex).replace(" ", "")
                    email = it.getString(emailIndex)

                    contacts.contactId = id
                    contacts.contactName = name
                    contacts.contactNumber = number
                    contacts.contactNumberList = arrayListOf(number)
                    contacts.contactEmail = email

                    Log.d("KontactEx", email)

                    if (contactMap[id] != null) {
                        val list = contactMap[id]?.contactNumberList!!
                        if (!list.contains(number))
                            list.add(number)
                        contacts.contactNumberList = list
                    } else {
                        contactMap[id] = contacts
                    }
                }
                it.close()
            }
            onComplete {
                val fetchingTime = System.currentTimeMillis() - startTime
                log("Fetching Completed in $fetchingTime ms")
                onCompleted.invoke(filterContactsFromMap(contactMap))
            }
            return@doAsyncResult
        }
    }

    private fun filterContactsFromMap(contactMap: MutableMap<String, MyContacts>): MutableList<MyContacts> {
        val myKontacts: MutableList<MyContacts> = arrayListOf()
        val phoneList = arrayListOf<String>()
        contactMap.entries.forEach {
            val contact = it.value

            val isUriEnable = KontactPickerUI.getPickerItem().includePhotoUri
//            val isLargeUriEnable = KontactPickerUI.getPickerItem().getLargePhotoUri
            var photoUri: Uri? = null
            if (isUriEnable) {
                photoUri = getContactImageUri(contact.contactId?.toLong()!!)
//                if (isLargeUriEnable)
//                    getContactImageLargeUri(contact.contactId?.toLong()!!)
//                else

            }

            contact.contactNumberList.forEach { number ->
                if (!phoneList.contains(number)) {
                    val newContact = MyContacts(
                        contact.contactId,
                        contact.contactName,
                        number, false, photoUri,
                        contact.contactNumberList
                    )
                    myKontacts.add(newContact)
                    phoneList.add(number)
                }
            }
        }
        myKontacts.sortBy {
            it.contactName
        }
        return myKontacts
    }

}