package com.donsdirectory.mobile.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.donsdirectory.mobile.R
import com.donsdirectory.mobile.adapters.ContactAdapter
import com.donsdirectory.mobile.databinding.ActivityMainBinding
import com.donsdirectory.mobile.lib.KontactPicker
import com.donsdirectory.mobile.model.*
import com.donsdirectory.mobile.util.init

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class MainActivity : AppCompatActivity() {
    private var myContacts: ArrayList<Contact>? = ArrayList()
    private var contactsAdapter: ContactAdapter? = null

    val debugModeCheck = MutableLiveData<Boolean>()
    val imageModeGroup = MutableLiveData<Int>()
    val selectionTickViewGroup = MutableLiveData<Int>()
    val selectionModeGroup = MutableLiveData<Int>()
    private var colorDefault: Int? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        binding = DataBindingUtil.setContentView(this,
//            R.layout.activity_main
//        )
//        binding.activity = this
//        binding.lifecycleOwner = this

        contactsAdapter =
            ContactAdapter(myContacts)
        binding.recyclerView.init(applicationContext)
        binding.recyclerView.adapter = contactsAdapter

        binding.kontactPickerBtn.setOnClickListener { openKontactPicker() }
        binding.getAllKontactBtn.setOnClickListener { showAllKontacts() }
//        binding.btnColorPicker.setOnClickListener { openColorPicker() }
    }

    private fun showAllKontacts() {
        val startTime = System.currentTimeMillis()
//        binding.progressBar.show()
        myContacts?.clear()
        contactsAdapter?.updateList(myContacts)
        KontactPicker.getAllKontactsWithUri(this) {
//            binding.progressBar.hide()
            for (contact in it) {
                myContacts?.add(
                    Contact(
                        contact.contactName,
                        contact.contactNumber,
                        contact.photoUri
                    )
                )
            }
            contactsAdapter?.updateList(myContacts)

            val fetchingTime = System.currentTimeMillis() - startTime
            Log.d("Main","Fetching Completed in $fetchingTime ms")
        }
    }

    private fun openKontactPicker() {
        val item = KontactPickerItem().apply {
            debugMode = debugModeCheck.value ?: false
            //            textBgColor = ContextCompat.getColor(this@MainActivity, R.color.colorBlue100)
            colorDefault?.let { textBgColor = it }
            includePhotoUri = true
            themeResId = R.style.CustomTheme
            imageMode = when (imageModeGroup.value) {
                0 -> ImageMode.None
                1 -> ImageMode.TextMode
                2 -> ImageMode.UserImageMode
                else -> ImageMode.None
            }
            selectionTickView = when (selectionTickViewGroup.value) {
                0 -> SelectionTickView.SmallView
                1 -> SelectionTickView.LargeView
                else -> SelectionTickView.SmallView
            }
            selectionMode = when (selectionModeGroup.value) {
                0 -> SelectionMode.Single
                1 -> SelectionMode.Multiple
                else -> SelectionMode.Multiple
            }
        }

        KontactPicker()
            .startPickerForResult(this, item, 3000)
    }

//    private fun openColorPicker() {
//        MaterialDialog(this).show {
//            title(R.string.colors)
//            colorChooser(ColorPalette.Primary) { _, color ->
//                colorDefault = color
//                log("color: $color")
//                binding.btnColorPicker.background = color.toDrawable()
//            }
//            positiveButton(R.string.select)
//            negativeButton(R.string.select_none) {
//                binding.btnColorPicker.background = ContextCompat.getDrawable(
//                    this@MainActivity, android.R.color.darker_gray
//                )
//                colorDefault = null
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 3000) {
            val list = KontactPicker.getSelectedKontacts(data)
//            binding.titleSelectedContacts.setText(R.string.selected_contacts)
            myContacts = arrayListOf()
            if (list != null) {
                for (contact in list) {
                    myContacts?.add(
                        Contact(
                            contact.contactName,
                            contact.contactNumber,
                            contact.photoUri
                        )
                    )
                }
            }
            contactsAdapter?.updateList(myContacts)
        }
    }

}