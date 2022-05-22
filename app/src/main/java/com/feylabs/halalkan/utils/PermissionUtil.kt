package com.feylabs.halalkan.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.IntDef
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.feylabs.halalkan.data.local.MyPreference


enum class PermissionActivityFlow(val category: String, val code: Int) {
    TRANSLATE_MIC("TranslateMic", 123)
}

data class PermissionModel(
    val name: String,
    val isDeniedForever: Boolean,
    val isDenied: Boolean,
    val isGranted: Boolean
)

class PermissionUtil {

    companion object {

        const val PERM_CODE_TRANSLATE_MIC = 1221

        const val PER_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
        const val PER_WRITE_EX_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val PER_READ_EX_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

        @IntDef(GRANTED, DENIED, BLOCKED, NEVER_ASKED)
        annotation class PermissionStatus

        const val GRANTED = 0
        const val DENIED = 1
        const val NEVER_ASKED = 2
        const val BLOCKED = 99

        @PermissionStatus
        fun getPermissionStatus(activity: Activity, androidPermissionName: String): Int {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    androidPermissionName
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        androidPermissionName
                    )
                ) {
                    val pref = MyPreference(activity).getPrefString("permCode${androidPermissionName}").orEmpty()
                    if (pref.isEmpty()){
                        return NEVER_ASKED
                    }else{
                        return BLOCKED
                    }
                } else return DENIED
            } else return GRANTED
        }

        fun permRecordAudio(mContext: Context) = ContextCompat.checkSelfPermission(
            mContext, Manifest.permission.RECORD_AUDIO
        )

        fun permCamera(mContext: Context) = ContextCompat.checkSelfPermission(
            mContext, Manifest.permission.CAMERA
        )

        fun permWriteInternalStorage(mContext: Context) = ContextCompat.checkSelfPermission(
            mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        fun permReadInternalStorage(mContext: Context) = ContextCompat.checkSelfPermission(
            mContext, Manifest.permission.READ_EXTERNAL_STORAGE
        )

        fun mRequestPermission(code: PermissionActivityFlow, activity: Activity) {
            when (code) {
                PermissionActivityFlow.TRANSLATE_MIC -> {
                    requestPermissionAction(
                        activity, arrayOf(PER_RECORD_AUDIO),
                        PermissionActivityFlow.TRANSLATE_MIC.code
                    )
                }
            }
        }

        private fun requestPermissionAction(activity: Activity, arrayOf: Array<String>, code: Int) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(PER_RECORD_AUDIO),
                code
            )

            // save to pref that this permission is ever asked
            arrayOf.forEachIndexed { index, permissionName ->
                MyPreference(activity).save("permCode$permissionName","noneno")
            }
        }
    }
}