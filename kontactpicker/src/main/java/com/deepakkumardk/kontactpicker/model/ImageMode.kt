package com.deepakkumardk.kontactpicker.model

/**
 * Created by Deepak Kumar on 25/05/2019
 */

sealed class ImageMode {
    object None : ImageMode() // default mode
    object TextMode : ImageMode()
//    object UserImageMode : ImageMode()
}