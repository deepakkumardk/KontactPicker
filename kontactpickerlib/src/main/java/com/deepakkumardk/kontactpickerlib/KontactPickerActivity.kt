package com.deepakkumardk.kontactpickerlib

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.deepakkumardk.kontactpickerlib.model.MyContacts
import com.deepakkumardk.kontactpickerlib.util.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast
import org.jetbrains.anko.yesButton

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactPickerActivity : AppCompatActivity() {
    private var myKontacts: MutableList<MyContacts> = mutableListOf()
    private var selectedKontacts: MutableList<MyContacts> = ArrayList()
    private lateinit var kontactsAdapter: KontactsAdapter
    private var debugMode = false

    private lateinit var fabDone: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyCustomTheme(KontactPickerUI.getTheme())
        setContentView(R.layout.activity_kontact_picker)

        debugMode = KontactPickerUI.getDebugMode()

        logInitialValues()
        initView()

        initToolbar()

        kontactsAdapter = KontactsAdapter(myKontacts, this::onItemClick)
        recyclerView.init(this)
        recyclerView.adapter = kontactsAdapter
        checkPermission()

        fabDone.setOnClickListener {
            val result = Intent()
            val list = getSelectedKontacts()
            result.putExtra(EXTRA_SELECTED_CONTACTS, list)
            setResult(Activity.RESULT_OK, result)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.kontact_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val search = menu?.findItem(R.id.action_search)?.actionView as SearchView

        search.queryHint = getString(R.string.search_hint)
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
            return@setOnCloseListener true
        }

        menu.findItem(R.id.action_search)
            ?.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(menuItem: MenuItem): Boolean {
                    animateToolbar()
                    return true
                }

                override fun onMenuItemActionCollapse(menuItem: MenuItem): Boolean {
                    kontactsAdapter.updateList(myKontacts)
                    val typedValue = TypedValue()
                    theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
                    val color = typedValue.data
                    supportActionBar?.setBackgroundDrawable(color.toDrawable())
                    return true
                }
            })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun animateToolbar() {
        val backgroundColorAnimator = ObjectAnimator.ofObject(
            supportActionBar, "backgroundColor", ArgbEvaluator(), 0x008577, 0xffffff
        )
        backgroundColorAnimator.duration = 200
        backgroundColorAnimator.start()
    }

    private fun logInitialValues() {
        if (debugMode) {
            log("DebugMode: $debugMode")
            log("SelectionTickVIew: ${KontactPickerUI.getSelectionTickView()}")
            log("Default Text Color: ${KontactPickerUI.getTextBgColor()}")
            log("Image Mode: ${KontactPickerUI.getImageMode()}")
        }
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val drawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        supportActionBar?.setHomeAsUpIndicator(drawable)
    }

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
            if (name?.contains(text, true) == true || number?.contains(text) == true) {
                tempList.add(contact)
            }
        }
        kontactsAdapter.updateList(tempList)
    }

    private fun checkPermission() {
        val contactReadPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        if (contactReadPermission) {
            loadContacts()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                RC_READ_CONTACTS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RC_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                alert(
                    "Please allow us to show contacts.",
                    "Permission Request"
                ) {
                    yesButton { checkPermission() }
                }.show()
            }
        }
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progress_bar)
        fabDone = findViewById(R.id.fab_done)
    }

    private fun loadContacts() {
        myKontacts.clear()
        progressBar.show()
        val startTime = System.currentTimeMillis()
        KontactEx().getAllContacts(this) {
            myKontacts.addAll(it)
            val fetchingTime = System.currentTimeMillis() - startTime
            if (debugMode) {
                longToast("Fetching Completed in $fetchingTime ms")
                log("Fetching Completed in $fetchingTime ms")
            }
            progressBar.hide()
            setSubtitle()
            kontactsAdapter.notifyDataSetChanged()
        }
    }
}