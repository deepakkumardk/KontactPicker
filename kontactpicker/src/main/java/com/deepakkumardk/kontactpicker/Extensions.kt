package com.deepakkumardk.kontactpicker

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

fun log(message: String) = Log.d("TAG_KONTACT_PICKER", message)