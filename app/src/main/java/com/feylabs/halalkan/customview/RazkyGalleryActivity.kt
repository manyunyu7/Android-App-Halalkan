package com.feylabs.halalkan.customview

import android.content.Intent
import androidx.fragment.app.Fragment
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig

class RazkyGalleryActivity : GalleryActivity() {

    companion object {
        /**
         * Open gallery with fragment starting the the activity to use onActivityResult
         * in fragment
         */
        fun openActivityFromFragment(fragment: Fragment, requestCode: Int, config: GalleryConfig?) {
            val intent = Intent(fragment.context, GalleryActivity::class.java)
            intent.putExtra("GALLERY_CONFIG", config)
            fragment.startActivityForResult(intent, requestCode)
        }
    }

}