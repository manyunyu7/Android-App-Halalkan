package com.feylabs.halalkan.utils

import android.net.Uri

object StringUtil {

    fun String.encodeUrl(): String {
        var url = ""
        url = this.replace("(", "%28")
        url = url.replace(")", "%29")
        url = url.replace(" ", "%20")
        return url
    }
}