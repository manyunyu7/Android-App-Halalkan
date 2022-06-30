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

    fun String.extractElementArrayFromStringArrayBE(): MutableList<String> {
        var some = this
        if (some.first().toString() == "[") {
            some = some.replaceFirst("[", "")
        }
        if (some.last().toString() == "]") {
            some = some.replace("]", "")
        }
        some = some.replace("'", "")
        return some.split(",").toTypedArray().toMutableList()
    }

    fun String.extractStringFromStringArrayBE(): String {
        val initialValue = this.extractElementArrayFromStringArrayBE()
        var initialString = ""
        initialValue.forEachIndexed { index, text ->
            initialString = "$initialString$text, "
        }
        val last = initialString.length
        // remove last comma
        initialString = initialString.dropLast(1)
        return initialString
    }

    fun String?.orMuskoEmpty(replacable:String): String {
        return if(this.isNullOrEmpty()){
            replacable
        }else{
            this
        }
    }
}