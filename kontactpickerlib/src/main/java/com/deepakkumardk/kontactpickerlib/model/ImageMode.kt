package com.deepakkumardk.kontactpickerlib.model

/**
 * Created by Deepak Kumar on 25/05/2019
 */

sealed class ImageMode {
    object None : ImageMode() // default mode   //0
    object TextMode : ImageMode()               //1
//    object UserImageMode : ImageMode()        //2
}