package com.deepakkumardk.kontactpicker

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.deepakkumardk.kontactpicker.databinding.ActivityMainBinding
import com.deepakkumardk.kontactpickerlib.KontactPicker
import com.deepakkumardk.kontactpickerlib.model.ImageMode
import com.deepakkumardk.kontactpickerlib.model.KontactPickerItem
import com.deepakkumardk.kontactpickerlib.model.SelectionTickView
import com.deepakkumardk.kontactpickerlib.util.hide
import com.deepakkumardk.kontactpickerlib.util.init
import com.deepakkumardk.kontactpickerlib.util.show
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class MainActivity : AppCompatActivity() {
    private var myContacts: ArrayList<Contact>? = ArrayList()
    private var contactsAdapter: ContactAdapter? = null

    val debugModeCheck = MutableLiveData<Boolean>()
    val imageModeCheck = MutableLiveData<Int>()
    val selectionModeCheck = MutableLiveData<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.activity = this
        binding.lifecycleOwner = this

        contactsAdapter = ContactAdapter(myContacts)
        recycler_view.init(applicationContext)
        recycler_view.adapter = contactsAdapter

        kontact_picker_btn.setOnClickListener { openKontactPicker() }
        get_all_kontact_btn.setOnClickListener { showAllKontacts() }

    }

    private fun showAllKontacts() {
        progress_bar.show()
        myContacts?.clear()
        contactsAdapter?.updateList(myContacts)
        KontactPicker.getAllKontacts(this) {
            progress_bar.hide()
            for (contact in it) {
                myContacts?.add(Contact(contact.contactName, contact.contactNumber))
            }
            contactsAdapter?.updateList(myContacts)
        }
    }

    private fun openKontactPicker() {
        val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            resources.getColor(R.color.colorBlue100, this.theme)
        } else {
            resources.getColor(R.color.colorBlue100)
        }

        val item = KontactPickerItem().apply {
            debugMode = debugModeCheck.value ?: false
            textBgColor = color
            imageMode = when (imageModeCheck.value) {
                0 -> ImageMode.None
                1 -> ImageMode.TextMode
                else -> ImageMode.None
            }
            selectionTickView = when (selectionModeCheck.value) {
                0 -> SelectionTickView.SmallView
                1 -> SelectionTickView.LargeView
                else -> SelectionTickView.SmallView
            }
        }

        KontactPicker().startPickerForResult(this, item, 3000)
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