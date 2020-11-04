package com.donsdirectory.mobile.util

import com.donsdirectory.mobile.model.KontactPickerItem

/**
 * Created by Deepak Kumar on 31/08/2019
 */

object KontactPickerUI {
    private lateinit var kontactPickerItem: KontactPickerItem

    fun getPickerItem() = kontactPickerItem

    fun setPickerUI(kontactPickerItem: KontactPickerItem) {
        KontactPickerUI.kontactPickerItem = kontactPickerItem
    }

    fun getDebugMode() = kontactPickerItem.debugMode

    fun getTheme() = kontactPickerItem.themeResId

    fun getImageMode() = kontactPickerItem.imageMode

    fun getSelectionTickView() = kontactPickerItem.selectionTickView

    fun getTextBgColor() = kontactPickerItem.textBgColor
}