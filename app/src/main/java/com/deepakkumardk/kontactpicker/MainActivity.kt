package com.deepakkumardk.kontactpicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.color.colorChooser
import com.deepakkumardk.kontactpicker.databinding.ActivityMainBinding
import com.deepakkumardk.kontactpickerlib.KontactPicker
import com.deepakkumardk.kontactpickerlib.model.ImageMode
import com.deepakkumardk.kontactpickerlib.model.KontactPickerItem
import com.deepakkumardk.kontactpickerlib.model.SelectionTickView
import com.deepakkumardk.kontactpickerlib.util.hide
import com.deepakkumardk.kontactpickerlib.util.init
import com.deepakkumardk.kontactpickerlib.util.log
import com.deepakkumardk.kontactpickerlib.util.show

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class MainActivity : AppCompatActivity() {
    private var myContacts: ArrayList<Contact> = ArrayList()
    private lateinit var contactsAdapter: ContactAdapter

    val debugModeCheck = MutableLiveData<Boolean>()
    val imageModeCheck = MutableLiveData<Int>()
    val selectionModeCheck = MutableLiveData<Int>()
    var colorDefault: Int? = null
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        binding.lifecycleOwner = this

        contactsAdapter = ContactAdapter(myContacts)
        binding.recyclerView.init(applicationContext)
        binding.recyclerView.adapter = contactsAdapter

        binding.kontactPickerBtn.setOnClickListener { openKontactPicker() }
        binding.getAllKontactBtn.setOnClickListener { showAllKontacts() }
        binding.btnColorPicker.setOnClickListener { openColorPicker() }
    }

    private fun showAllKontacts() {
        val startTime = System.currentTimeMillis()
        binding.progressBar.show()
        myContacts.clear()
        contactsAdapter.updateList(myContacts)
        KontactPicker.getAllKontactsWithUri(this, true) {
            binding.progressBar.hide()
            for (contact in it) {
                myContacts.add(
                    Contact(contact.contactName, contact.contactNumber, contact.photoUri)
                )
            }
            contactsAdapter.updateList(myContacts)

            val fetchingTime = System.currentTimeMillis() - startTime
            log("Fetching Completed in $fetchingTime ms")
        }
    }

    private fun openKontactPicker() {
        val item = KontactPickerItem().apply {
            debugMode = debugModeCheck.value ?: false
            //            textBgColor = ContextCompat.getColor(this@MainActivity, R.color.colorBlue100)
            colorDefault?.let {
                textBgColor = it
            }
            includePhotoUri = true
            themeResId = R.style.CustomTheme
            imageMode = when (imageModeCheck.value) {
                0 -> ImageMode.None
                1 -> ImageMode.TextMode
                2 -> ImageMode.UserImageMode
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

    private fun openColorPicker() {
        MaterialDialog(this).show {
            title(R.string.colors)
            colorChooser(ColorPalette.Primary) { _, color ->
                colorDefault = color
                log("color: $color")
                binding.btnColorPicker.background = color.toDrawable()
            }
            positiveButton(R.string.select)
            negativeButton(R.string.select_none) {
                binding.btnColorPicker.background = ContextCompat.getDrawable(
                    this@MainActivity, android.R.color.darker_gray
                )
                colorDefault = null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 3000) {
            val list = KontactPicker.getSelectedKontacts(data)
            binding.titleSelectedContacts.setText(R.string.selected_contacts)
            myContacts = arrayListOf()
            if (list != null) {
                for (contact in list) {
                    myContacts.add(
                        Contact(contact.contactName, contact.contactNumber, contact.photoUri)
                    )
                }
            }
            contactsAdapter.updateList(myContacts)
        }
    }

}