package com.deepakkumardk.kontactpicker.model

/**
 * Created by Deepak Kumar on 25/05/2019
 */

sealed class SelectionTickView {
    object SmallView : SelectionTickView()  //0
    object LargeView : SelectionTickView()  //1
}