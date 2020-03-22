package com.deepakkumardk.kontactpickerlib.model

/**
 * @author Deepak Kumar
 * @since 12/10/19
 */
sealed class SelectionMode {
    object Single : SelectionMode()
    object Multiple : SelectionMode()
    data class Custom(val limit:Int) : SelectionMode()
}