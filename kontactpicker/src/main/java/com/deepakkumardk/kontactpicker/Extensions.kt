package com.deepakkumardk.kontactpicker

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.onComplete
import java.io.ByteArrayInputStream
import java.io.IOException


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
        "F44336", "E91E63", "9C27B0", "673AB7", "3F51B5", "2196F3", "03A9F4", "00BCD4", "009688",
        "4CAF50", "8BC34A", "CDDC39", "FFEB3B", "FFC107", "FF9800", "FF5722", "9E9E9E", "607D8B"
    )
    return Color.parseColor("#${colorsList.random()}")
}

fun getTextDrawable(name: String): TextDrawable? {
    val initials = name[0].toString().capitalize()
    return TextDrawable.builder()
        .beginConfig()
        .width(1024)
        .height(1024)
        .bold()
        .endConfig()
        .buildRound(initials, generateRandomColor())
}

fun Context?.getContactPhoto(contactId: String, onSuccess: (Bitmap?) -> Unit) {
    doAsyncResult {
        var photo: Bitmap? = null
        try {
            val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
                this@getContactPhoto?.contentResolver,
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId.toLong())
            )

            inputStream?.let {
                photo = BitmapFactory.decodeStream(inputStream)
            }
            assert(inputStream != null)
            inputStream!!.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        onComplete {
            onSuccess(photo)
        }
    }
}

fun Context?.getPhoto(contactId: Long, onSuccess: (Bitmap?) -> Unit) {
    doAsyncResult {
        val contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        val photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
        val cursor =
            this@getPhoto?.contentResolver?.query(
                photoUri, arrayOf(
                    ContactsContract.Contacts.Photo.PHOTO
                ), null, null, null
            )
        cursor.use {
            if (it?.moveToFirst()!!) {
                val data = it.getBlob(0)
                data?.let {
                    onSuccess(
                        BitmapFactory.decodeStream(ByteArrayInputStream(data))
                    )
                }
            }
        }
    }
}

fun log(message: String) = Log.d("TAG_KONTACT_PICKER", message)