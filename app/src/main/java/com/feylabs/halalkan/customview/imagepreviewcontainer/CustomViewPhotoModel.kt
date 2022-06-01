package com.feylabs.halalkan.customview.imagepreviewcontainer

import android.net.Uri
import com.feylabs.halalkan.R
import java.io.File

data class CustomViewPhotoModel(
    var name: String = "",
    var url: String = "",
    var drawable: Int = R.drawable.bg_header_daylight,
    var id: Int = -99,
    var slug: String = "",
    var uri: String = "",
    var uriGlide: Uri? = null,
    var file: File? = null,
    var isDeletable:Boolean=false,
    var type: TypePhotoModel? = TypePhotoModel.URL,
    var onclicklistener: (()->Unit)? = null
)

enum class TypePhotoModel{
    URL,TELEGRAM_PHOTO_PICKER
}
