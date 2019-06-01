package com.deepakkumardk.kontactpicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.deepakkumardk.kontactpicker.model.ImageMode
import com.deepakkumardk.kontactpicker.model.SelectionTickView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class MainActivity : AppCompatActivity() {
    private var myContacts: ArrayList<Contact>? = ArrayList()
    private var contactsAdapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactsAdapter = ContactAdapter(myContacts)
        recycler_view.init(applicationContext)
        recycler_view.adapter = contactsAdapter

        kontact_picker_btn.setOnClickListener { openKontactPicker() }
    }

    private fun openKontactPicker() {
        KontactPicker.Builder(this)
            .setDebugMode(true)
            .setImageMode(ImageMode.None)
            .setSelectionTickView(SelectionTickView.SmallView)
            .showPickerForResult(3000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 3000) {
            val list = KontactPicker.getSelectedKontacts(data)
            title_selected_contacts.setText(R.string.selected_contacts)
            myContacts = arrayListOf()
            if (list != null) {
                for (contact in list) {
                    myContacts?.add(Contact(contact.contactName, contact.contactNumber))
                }
            }
            contactsAdapter?.updateList(myContacts)
        }
    }

}