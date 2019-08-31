package com.deepakkumardk.kontactpickerlib.util

object KontactPickerUI {
    var debugMode = false
    var imageMode = 0
    var selectionTickView = 0
    var textBgColor = 0

    fun setPickerUI(debugMode: Boolean, imageMode: Int, selectionTickView: Int, textBgColor: Int) {
        this.debugMode = debugMode
        this.imageMode = imageMode
        this.selectionTickView = selectionTickView
        this.textBgColor = textBgColor
    }
}