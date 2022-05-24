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

enum class PermissionActivityFlow(val category: String, val code: Int) {
    TRANSLATE_MIC("TranslateMic", 123),
    LOCATION_INIT("LocationInit", 41)
}

enum class MyPermissionStatus {
    GRANTED,DENIED,BLOCKED,NEVER_ASKED
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

        const val PER_LOCATION_FINE = Manifest.permission.ACCESS_FINE_LOCATION
        const val PER_LOCATION_COARSE = Manifest.permission.ACCESS_COARSE_LOCATION
        const val PER_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
        const val PER_WRITE_EX_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val PER_READ_EX_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

        fun List<MyPermissionStatus>.isNotGranted() : Boolean{
            return this.contains(DENIED) && this.contains(BLOCKED) && this.contains(NEVER_ASKED)
        }

        fun MyPermissionStatus.isNotGranted() : Boolean{
            return this == (DENIED) || this == BLOCKED || this == NEVER_ASKED
        }

        fun MyPermissionStatus.isGranted() : Boolean{
            return this == (GRANTED)
        }

        fun MyPermissionStatus.isBlocked() : Boolean{
            return this == BLOCKED
        }

        fun getPermissionStatus(activity: Activity, androidPermissionName: String): MyPermissionStatus {
            if (ContextCompat.checkSelfPermission(
                    activity,
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
                        return MyPermissionStatus.NEVER_ASKED
                    }else{
                        return MyPermissionStatus.BLOCKED
                    }
                } else return MyPermissionStatus.DENIED
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
                PermissionActivityFlow.LOCATION_INIT -> {
                    requestPermissionAction(
                        activity, arrayOf(PER_LOCATION_COARSE, PER_LOCATION_FINE),
                        PermissionActivityFlow.LOCATION_INIT.code
                    )
                }
            }
        }

        private fun requestPermissionAction(activity: Activity, arrayOf: Array<String>, code: Int) {
            ActivityCompat.requestPermissions(
                activity, arrayOf,
                code
            )

            // save to pref that this permission is ever asked
            arrayOf.forEachIndexed { index, permissionName ->
                MyPreference(activity).save("permCode$permissionName","noneno")
            }
        }

        fun openSetting(context:Context){
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package",context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }
}