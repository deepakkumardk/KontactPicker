package com.deepakkumardk.kontactpicker

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var myContacts: ArrayList<Contact>? = ArrayList()
    private var contactsAdapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactsAdapter = ContactAdapter(myContacts)
        recycler_view.init(applicationContext)
        recycler_view.adapter = contactsAdapter

        kontact_picker_btn.setOnClickListener { checkPermission() }
    }

    private fun openKontactPicker() {
        KontactPicker.Builder(this)
            .showPickerForResult(3000)
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        when {
            contactReadPermission -> {
                openKontactPicker()
            }
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), 2001)
                }
            }
        }
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