package com.deepakkumardk.kontactpicker.model

/**
 * Created by Deepak Kumar on 25/05/2019
 */

sealed class SelectionTickView {
    object SmallView : SelectionTickView()
    object LargeView : SelectionTickView()
}