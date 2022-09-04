package com.feylabs.halalkan.utils

import com.feylabs.halalkan.utils.ImageViewUtils.imgFullPath
import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtil {


    fun String.loadMuskoImage(): String {
        return this.decodeMuskoUrl().imgFullPath()
    }

    fun String.decodeMuskoUrl(): String {
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

    fun String?.orMuskoEmpty(replacable: String = ""): String {
        return if (this.isNullOrEmpty()) {
            replacable
        } else {
            this
        }
    }

    fun String.checkHourFormat(): Boolean {
        val p: Pattern = Pattern.compile("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]\$")
        val m: Matcher = p.matcher(this)
        return m.matches()
    }

    fun Any.prettyPrint(): String {

        var indentLevel = 0
        val indentWidth = 4

        fun padding() = "".padStart(indentLevel * indentWidth)

        val toString = toString()

        val stringBuilder = StringBuilder(toString.length)

        var i = 0
        while (i < toString.length) {
            when (val char = toString[i]) {
                '(', '[', '{' -> {
                    indentLevel++
                    stringBuilder.appendLine(char).append(padding())
                }
                ')', ']', '}' -> {
                    indentLevel--
                    stringBuilder.appendLine().append(padding()).append(char)
                }
                ',' -> {
                    stringBuilder.appendLine(char).append(padding())
                    // ignore space after comma as we have added a newline
                    val nextChar = toString.getOrElse(i + 1) { char }
                    if (nextChar == ' ') i++
                }
                else -> {
                    stringBuilder.append(char)
                }
            }
            i++
        }

        return stringBuilder.toString()
    }
}