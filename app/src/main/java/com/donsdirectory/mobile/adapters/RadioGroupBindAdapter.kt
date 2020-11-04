package com.donsdirectory.mobile.adapters

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.databinding.*

@BindingMethods(
    BindingMethod(
        type = RadioGroup::class,
        attribute = "android:onRadioChanged",
        method = "setOnCheckedChangeListener"
    )
)
object RadioGroupBindAdapter {
    @BindingAdapter("checkedPosition")
    @JvmStatic
    fun setCheckPosition(radioGroup: RadioGroup, position: Int) {
        if (getRadioGroupIndex(
                radioGroup
            ) != position) {
            val childAt = radioGroup.getChildAt(position) as? RadioButton
            childAt?.isChecked = true
        }
    }

    @BindingAdapter("checkedPositionAttrChanged")
    @JvmStatic
    fun setInverseBindingListener(radioGroup: RadioGroup, listener: InverseBindingListener) {
        radioGroup.setOnCheckedChangeListener { _, _ ->
            listener.onChange()
        }
    }

    @InverseBindingAdapter(attribute = "checkedPosition", event = "checkedPositionAttrChanged")
    @JvmStatic
    fun getCheckedPosition(radioGroup: RadioGroup): Int {
        return getRadioGroupIndex(
            radioGroup
        )
    }

    @JvmStatic
    private fun getRadioGroupIndex(group: RadioGroup): Int {
        val radioButtonID = group.checkedRadioButtonId
        val radioButton = group.findViewById<View>(radioButtonID)
        return group.indexOfChild(radioButton)
    }
}