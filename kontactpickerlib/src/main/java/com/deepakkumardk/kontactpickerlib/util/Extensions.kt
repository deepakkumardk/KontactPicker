package com.deepakkumardk.kontactpickerlib.util

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import java.util.*

/**
 * Extension functions
 * Created by Deepak Kumar on 25/05/2019
 */

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View?.hide() {
    this?.visibility = View.GONE
}

fun RecyclerView.init(context: Context) {
    this.apply {
        hasFixedSize()
        layoutManager = LinearLayoutManager(context)
    }
}

fun generateRandomColor(): Int {
    val colorsList = arrayOf(
        "039BE5", "0F9D58", "4285F4", "FF5722", "DB4437", "689F38", "009688", "DB4437", "3F51B5",
        "9C27B0", "4E342E", "F50057", "42A5F5", "009688", "9E9D24", "00C853", "BF360C", "37474F"
    )
    return Color.parseColor("#${colorsList.random()}")
}

fun getTextDrawable(name: String): TextDrawable? {
    var color = KontactPickerUI.getTextBgColor()
    if (color == null) color = generateRandomColor()
    val initials = name[0].toString().toUpperCase(Locale.ENGLISH)
    return TextDrawable.builder()
        .beginConfig()
        .width(1024)
        .height(1024)
        .bold()
        .endConfig()
        .buildRound(initials, color)
}

fun getContactImageUri(contactId: Long): Uri? {
    val person = ContentUris.withAppendedId(
        ContactsContract.Contacts.CONTENT_URI, contactId
    )
    return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
}

fun Activity?.applyCustomTheme(themeId: Int?) {
    themeId?.let {
        this?.setTheme(themeId)
    }
}

fun log(message: String) = Log.d("TAG_KONTACT_PICKER", message)