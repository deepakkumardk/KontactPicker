package com.deepakkumardk.kontactpicker

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.deepakkumardk.kontactpicker.model.ImageMode
import com.deepakkumardk.kontactpicker.model.MyContacts
import com.deepakkumardk.kontactpicker.model.SelectionTickView

/**
 * Created by Deepak Kumar on 25/05/2019
 */

class KontactPicker {

    companion object {
        /**
         * returns a list of MyContacts
         */
        fun getSelectedKontacts(data: Intent?): ArrayList<MyContacts>? {
            return data?.getParcelableArrayListExtra<MyContacts>("extra_selected_contacts")
        }

        /**
         * returns a list of selected contact's phone number
         */
        fun getSelectedPhoneList(data: Intent?): ArrayList<String?> {
            val kontactsList = getSelectedKontacts(data)
            val phoneList = arrayListOf<String?>()
            kontactsList?.let {
                for (contact in it) {
                    phoneList.add(contact.contactNumber)
                }
            }
            return phoneList
        }
    }

    class Builder() : Parcelable {
        private var activity: Activity? = null
        private var fragment: Fragment? = null
        var debugMode = 0
        var imageMode = 0
        var selectionTickView = 0

        @Suppress("UNUSED_PARAMETER")
        constructor(parcel: Parcel) : this() {
            this.debugMode = parcel.readInt()
            this.imageMode = parcel.readInt()
            this.selectionTickView = parcel.readInt()
        }

        constructor(activity: Activity?) : this() {
            this.activity = activity
        }

        @Suppress("unused")
        constructor(fragment: Fragment?) : this() {
            this.fragment = fragment
        }

        fun setDebugMode(debugMode: Boolean): Builder {
            this.debugMode = if (debugMode) 1 else 0
            log("debugMode ${this.debugMode}")
            return this
        }

        fun setSelectionTickView(selectionTickView: SelectionTickView): Builder {
            this.selectionTickView = when (selectionTickView) {
                SelectionTickView.SmallView -> 0
                SelectionTickView.LargeView -> 1
            }
            log("tickView ${this.selectionTickView}")
            return this
        }

        fun setImageMode(imageMode: ImageMode): Builder {
            this.imageMode = when (imageMode) {
                ImageMode.None -> 0
                ImageMode.TextMode -> 1
                ImageMode.UserImageMode -> 2
            }
            log("imageMode ${this.imageMode}")
            return this
        }

        fun showPickerForResult(requestCode: Int) {
            val intent = Intent(this.activity, KontactPickerActivity::class.java)
            intent.putExtra("builder", this)

            this.activity.let {
                it?.startActivityForResult(intent, requestCode)
            }
            this.fragment.let {
                it?.startActivityForResult(intent, requestCode)
            }
        }


        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeInt(debugMode)
            dest?.writeInt(imageMode)
            dest?.writeInt(selectionTickView)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Builder> {
            override fun createFromParcel(parcel: Parcel): Builder {
                return Builder(parcel)
            }

            override fun newArray(size: Int): Array<Builder?> {
                return arrayOfNulls(size)
            }
        }
    }

}