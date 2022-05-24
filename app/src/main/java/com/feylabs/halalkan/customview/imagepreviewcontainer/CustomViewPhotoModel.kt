package com.feylabs.halalkan.customview.imagepreviewcontainer

import com.feylabs.halalkan.R

data class CustomViewPhotoModel(
    var name: String = "",
    var url: String = "",
    var drawable: Int = R.drawable.bg_header_daylight,
    var id: Int = -99,
    var slug: String = "",
    var onclicklistener : (()->Unit)? = null
)
