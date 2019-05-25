package com.deepakkumardk.kontactpicker

import android.app.Activity
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.deepakkumardk.kontactpicker.model.MyContacts

class KontactPicker {

    companion object {
        fun getSelectedKontacts(data: Intent?): ArrayList<MyContacts>? {
            return data?.getParcelableArrayListExtra<MyContacts>("extra_selected_contacts")
        }
    }

    class Builder() : Parcelable {
        private var activity: Activity? = null
        private var fragment: Fragment? = null

        @Suppress("UNUSED_PARAMETER")
        constructor(parcel: Parcel) : this()

        constructor(activity: Activity?) : this() {
            this.activity = activity
        }

        @Suppress("unused")
        constructor(fragment: Fragment?) : this() {
            this.fragment = fragment
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