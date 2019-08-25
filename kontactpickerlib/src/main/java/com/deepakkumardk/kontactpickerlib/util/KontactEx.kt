package com.deepakkumardk.kontactpickerlib.util

import android.app.Activity
import android.provider.ContactsContract
import com.deepakkumardk.kontactpickerlib.log
import com.deepakkumardk.kontactpickerlib.model.MyContacts
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete

/**
 * Created by Deepak Kumar on 23/08/2019
 */

fun Activity.getAllContacts(onCompleted: (MutableList<MyContacts>) -> Unit) {
    val myKontacts: MutableList<MyContacts> = arrayListOf()
    val startTime = System.currentTimeMillis()
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

    val cr = contentResolver
    doAsyncResult {
        cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
            null, null, null
        )?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            var name: String
            var number: String
            while (it.moveToNext()) {
                val contacts = MyContacts()
                name = it.getString(nameIndex)
                number = it.getString(numberIndex)
                contacts.contactName = name
                contacts.contactNumber = number
                myKontacts.add(contacts)
            }
            it.close()
        }
        onComplete {
            myKontacts.sortBy {
                it.contactName
            }
            val fetchingTime = System.currentTimeMillis() - startTime
            log("Fetching Completed in $fetchingTime ms")
            onCompleted.invoke(myKontacts)
        }
        return@doAsyncResult myKontacts
    }
}