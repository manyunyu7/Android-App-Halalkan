package com.feylabs.halalkan.utils.base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    fun showToast(text: String, isLong: Boolean = false) {
        var duration = Toast.LENGTH_LONG
        if (!isLong)
            duration = Toast.LENGTH_SHORT
        Toast.makeText(this, text, duration).show()
    }

}