package com.deepakkumardk.kontactpicker.model

sealed class SelectionTickView {
    object SmallView : SelectionTickView()
    object LargeView : SelectionTickView()
}