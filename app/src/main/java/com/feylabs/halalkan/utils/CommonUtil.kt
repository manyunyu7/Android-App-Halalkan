package com.feylabs.halalkan.utils

import android.content.Context
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.feylabs.halalkan.customview.searchwithimage.SearchWithImageModel
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.murgupluoglu.flagkit.FlagKit
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

object CommonUtil {
    val flagMap = mutableMapOf<String, String>()


    fun isLoggedIn(context: Context): Boolean {
        val check = MyPreference(context).getToken() ?: ""
        return check.isEmpty().not()
    }

    fun View.showSnackbar(text: String, type: SnackbarType = SnackbarType.INFO) {
        UtilSnackbar.showSnackbar(this, text, type)
    }

    fun DialogFragment.showSingleDialog(fm: FragmentManager, tag: String = this.javaClass.name){
        if (fm.findFragmentByTag(tag) == null) show(fm, tag)
    }

    fun BottomSheetDialogFragment.showSingleDialog(fm: FragmentManager, tag: String = this.javaClass.name){
        if (fm.findFragmentByTag(tag) == null) show(fm, tag)
    }

    fun viewGone(view: View) {
        view.visibility = View.GONE
    }

    fun viewInvisible(view: View) {
        view.visibility = View.GONE
    }

    fun View.makeVisible() {
        viewVisible(this)
    }

    fun View.makeGone() {
        viewGone(this)
    }

    fun View.makeInvisible() {
        viewInvisible(this)
    }

    fun viewVisible(view: View) {
        view.visibility = View.VISIBLE
    }


}