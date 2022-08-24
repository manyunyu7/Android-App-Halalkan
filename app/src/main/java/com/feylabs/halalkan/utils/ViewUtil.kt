package com.feylabs.halalkan.utils

import android.content.Context
import android.util.TypedValue
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

object ViewUtil {
    fun Int.dpToPixels(context: Context):Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    )
}