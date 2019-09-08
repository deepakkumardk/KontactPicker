package com.deepakkumardk.kontactpickerlib.model

/**
 * Created by Deepak Kumar on 31/08/2019
 */

class KontactPickerItem {
    var debugMode = false
    var themeResId: Int? = null
    var textBgColor: Int? = null
    var includePhotoUri = false
    var imageMode: ImageMode = ImageMode.None
    var selectionTickView: SelectionTickView = SelectionTickView.SmallView
}