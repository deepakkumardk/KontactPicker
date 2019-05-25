package com.deepakkumardk.kontactpicker

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.deepakkumardk.kontactpicker.model.MyContacts
import kotlinx.android.synthetic.main.activity_kontact_picker.*
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.longToast
import org.jetbrains.anko.onComplete
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class KontactPickerActivity : AppCompatActivity() {
    private var myContacts: MutableList<MyContacts> = ArrayList()
    private var kontactsAdapter: KontactsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kontact_picker)

        val intent = intent
        val builder = intent.getParcelableExtra<KontactPicker.Builder>("builder")

        initToolbar()
        kontactsAdapter = KontactsAdapter(myContacts) { contact, position, view ->
            onItemClick(contact, position, view)
        }
        recycler_view.init(applicationContext)
        recycler_view.adapter = kontactsAdapter
        loadContacts()

        fab_done.onClick {
            val result = Intent()
            val list = kontactsAdapter?.getSelectedKontacts()
            result.putExtra("extra_selected_contacts", list)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(R.drawable.ic_arrow_back))
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
            //            kontactsAdapter?.updateList(myContacts)
            return@setOnCloseListener true

        }

        menu.findItem(R.id.action_search)?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                kontactsAdapter?.updateList(myContacts)
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> toast("Search")
            else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getKontactsResult(list: List<MyContacts>?): ArrayList<MyContacts> {
        val kontactsResults = arrayListOf<MyContacts>()
        list.let {
            for (contact in it!!) {

            }
        }
        return kontactsResults
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onItemClick(contact: MyContacts?, position: Int, view: View) = setSubtitle()

    private fun setSubtitle() {
        supportActionBar?.subtitle = "${kontactsAdapter?.getSelectedKontacts()?.size} of ${myContacts.size} Contacts"
    }

    private fun filterContacts(text: String) {
        val tempList = arrayListOf<MyContacts>()
        for (contact in myContacts) {
            val name = contact.contactName
            val number = contact.contactNumber
            if (name?.contains(text, true)!! || number?.contains(text)!!) {
                tempList.add(contact)
            }
        }
        kontactsAdapter?.updateList(tempList)
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
                    myContacts.add(contacts)
                }
            }
            onComplete {
                myContacts.sortBy {
                    it.contactName
                }
                val fetchingTime = System.currentTimeMillis() - startTime
                longToast("Fetching Completed in $fetchingTime ms")
                setSubtitle()
                progress_bar.hide()
                kontactsAdapter?.notifyDataSetChanged()
            }
            return@doAsyncResult myContacts
        }
    }

}