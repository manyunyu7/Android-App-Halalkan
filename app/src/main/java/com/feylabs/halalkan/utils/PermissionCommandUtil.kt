package com.feylabs.halalkan.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.utils.MyPermissionStatus.*


class PermissionCommandUtil {

    companion object {
        fun micTranslate(activity: Activity) {
            PermissionUtil.mRequestPermission(
                PermissionActivityFlow.TRANSLATE_MIC,
                activity
            )
        }
    }

}