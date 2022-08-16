package com.feylabs.halalkan.utils.base

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.request.PermissionRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment() {

    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)

    abstract fun initUI()
    abstract fun initObserver()
    abstract fun initAction()
    abstract fun initData()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showActionBar()
        initObserver()
        initData() // should first
        initAction()
        initUI()
    }

    fun getRootView() = view

    fun getMFragmentManager() = parentFragmentManager

    fun showToast(text: String, isLong: Boolean = false) {
        var duration = Toast.LENGTH_LONG
        if (!isLong)
            duration = Toast.LENGTH_SHORT
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    fun showSnackbar(text: String, type: SnackbarType = SnackbarType.INFO) {
        UtilSnackbar.showSnackbar(getRootView(), text, type)
    }

    fun muskoPref() = MyPreference(requireContext())

    fun hideActionBar() {
        requireActivity().actionBar?.hide()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    fun showActionBar() {
        requireActivity().actionBar?.show()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
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

    fun enabledBackButton(status: Boolean) {
        // handle back on preview
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    isEnabled = status
                }
            }
            )
    }

    fun getMuskoDrawable(source: Int): Drawable {
        return ContextCompat.getDrawable(requireActivity(), source)!!
    }

    fun setBottomMenuActive(view:View){
        if(view is TextView){
            view.setTextColor(Color.parseColor(colorActive()))
        }

        if(view is ImageView){
            view.setColorFilter(ContextCompat.getColor(requireContext(), R.color.menu_bottom_active));
        }
    }

    fun updateUserData(userData: UserModel) {
        muskoPref().saveLoginData(
            userId = userData.id.toString(),
            name = userData.name,
            email = userData.email,
            photo = userData.getPhotoPath(),
            role = userData.rolesId.toString()
        )
    }

    fun getChoosenResto() =  MyPreference(requireContext()).getPrefString("CHOSEN_RESTO").toString()

    fun colorActive() = "#156DBE"

    fun setLayoutManagerGridVertical(spanCount:Int=2) = GridLayoutManager(requireContext(),spanCount,
        GridLayoutManager.VERTICAL,false)

    fun setLayoutManagerGridHorizontal(spanCount:Int=2) = GridLayoutManager(requireContext(),spanCount,
        GridLayoutManager.HORIZONTAL,false)

    fun setLayoutManagerLinear() = LinearLayoutManager(requireContext())
    fun setLayoutManagerHorizontal() = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)


}