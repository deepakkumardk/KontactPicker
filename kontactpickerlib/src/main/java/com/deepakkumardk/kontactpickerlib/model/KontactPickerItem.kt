package com.deepakkumardk.kontactpickerlib.model

/**
 * Created by Deepak Kumar on 31/08/2019
 */

class KontactPickerItem {
    var debugMode = false
    var themeResId: Int? = null
    var textBgColor: Int? = null
    var includePhotoUri = false
    var getLargePhotoUri = false
    var imageMode: ImageMode = ImageMode.None
    var selectionTickView: SelectionTickView = SelectionTickView.SmallView

    var selectionMode: SelectionMode = SelectionMode.Multiple

    //Text messages
    var limitMsg = "You can\'t select more than %s contacts"
    var permissionDeniedTitle = "Contact Permission Request"
    var permissionDeniedMsg = "Please allow us to show contacts."
}