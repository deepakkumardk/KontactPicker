package com.donsdirectory.lib.util

import com.donsdirectory.lib.model.KontactPickerItem

/**
 * Created by Deepak Kumar on 31/08/2019
 */

object KontactPickerUI {
    private lateinit var kontactPickerItem: KontactPickerItem

    fun getPickerItem() = kontactPickerItem

    fun setPickerUI(kontactPickerItem: KontactPickerItem) {
        this.kontactPickerItem = kontactPickerItem
    }

    fun getDebugMode() = kontactPickerItem.debugMode

    fun getTheme() = kontactPickerItem.themeResId

    fun getImageMode() = kontactPickerItem.imageMode

    fun getSelectionTickView() = kontactPickerItem.selectionTickView

    fun getTextBgColor() = kontactPickerItem.textBgColor
}