package com.deepakkumardk.kontactpicker

import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.deepakkumardk.kontactpicker.model.MyContacts
import kotlinx.android.synthetic.main.activity_kontact_picker.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.yesButton

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactPickerActivity : AppCompatActivity() {
    private var myKontacts: MutableList<MyContacts> = ArrayList()
    private var selectedKontacts: MutableList<MyContacts> = ArrayList()
    private var kontactsAdapter: KontactsAdapter? = null
    private var debugMode = false
    private var smallView = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kontact_picker)

        val intent = intent
        val builder = intent.getParcelableExtra<KontactPicker.Builder>("builder")
        debugMode = builder.debugMode == 1
        smallView = builder.selectionTickView == 0
        logInitialValues()

        initToolbar()
        kontactsAdapter = KontactsAdapter(myKontacts, smallView) { contact, position, view ->
            onItemClick(contact, position, view)
        }
        recycler_view.init(applicationContext)
        recycler_view.adapter = kontactsAdapter
        checkPermission()

        fab_done.onClick {
            val result = Intent()
            val list = getSelectedKontacts()
            result.putExtra("extra_selected_contacts", list)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.kontact_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search = menu?.findItem(R.id.action_search)?.actionView as SearchView

        search.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                filterContacts(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }
        })
        search.setOnCloseListener {
            //            kontactsAdapter?.updateList(myKontacts)
            return@setOnCloseListener true

        }

        menu.findItem(R.id.action_search)?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                kontactsAdapter?.updateList(myKontacts)
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.action_search -> log("Search")
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logInitialValues(): Unit {
        log("DebugMode: $debugMode")
        log("SelectionTickVIew: $smallView")
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back))
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onItemClick(contact: MyContacts?, position: Int, view: View) {
        contact?.isSelected = !contact?.isSelected!!
        when (contact.isSelected) {
            true -> {
                view.show()
                selectedKontacts.add(contact)
            }
            false -> {
                view.hide()
                selectedKontacts.remove(contact)
            }
        }
        setSubtitle()
    }

    private fun getSelectedKontacts(): ArrayList<MyContacts> {
        val list = arrayListOf<MyContacts>()
        for (contact in this.selectedKontacts) {
            if (contact.isSelected)
                list.add(contact)
        }
        return list
    }

    private fun setSubtitle() {
        supportActionBar?.subtitle = "${getSelectedKontacts().size} of ${myKontacts.size} Contacts"
    }

    private fun filterContacts(text: String) {
        val tempList = arrayListOf<MyContacts>()
        for (contact in myKontacts) {
            val name = contact.contactName
            val number = contact.contactNumber
            if (name?.contains(text, true)!! || number?.contains(text)!!) {
                tempList.add(contact)
            }
        }
        kontactsAdapter?.updateList(tempList)
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        when {
            contactReadPermission -> {
                loadContacts()
            }
            else -> when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), RC_READ_CONTACTS)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                    loadContacts()
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    alert(title= "Permission Request",message = "Please allow us to show contacts.") {
                        yesButton { checkPermission() }
                    }.show()
                }
                return

            }
        }
    }

    private fun loadContacts() {
        progress_bar.show()
        val startTime = System.currentTimeMillis()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cr = contentResolver
        val myContactsList = doAsyncResult {
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

                if (debugMode) {
//                    longToast("Fetching Completed in $fetchingTime ms")
                    log("Fetching Completed in $fetchingTime ms")
                }
                setSubtitle()
                progress_bar.hide()
                kontactsAdapter?.notifyDataSetChanged()
            }
            return@doAsyncResult myKontacts
        }
    }

}