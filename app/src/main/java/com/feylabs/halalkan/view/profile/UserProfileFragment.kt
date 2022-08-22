package com.feylabs.halalkan.view.profile

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.databinding.FragmentUserProfileBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar
import com.feylabs.halalkan.view.auth.AuthViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class UserProfileFragment : BaseFragment() {

    val viewModel: AuthViewModel by viewModel()
    private var _binding: FragmentUserProfileBinding? = null

    // save temporary user list
    private var listUsers = mutableListOf<UserResponse.UserResponseItem>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {
        binding.btnLogout.setOnClickListener {
            muskoPref().clearPreferences()
            findNavController().navigate(R.id.navigation_newHomeFragment)
        }
    }

    override fun initData() {

    }

    override fun initUI() {

        binding.loadingAnim.makeGone()

        binding.bottomNav.apply {
            setBottomMenuActive(tvProfile)
            setBottomMenuActive(logoProfile)
            btnMenuProfile.setOnClickListener {
                findNavController().navigate(R.id.navigation_userProfileFragment)
            }
            btnMenuHome.setOnClickListener {
                findNavController().navigate(R.id.navigation_newHomeFragment)
            }
        }

        val userData  = muskoPref().getUserData()
        binding.ivMainImage.loadImageFromURL(requireContext(),userData.getPhotoPath())
        binding.labelInfoProfileUserName.text=userData.name.orMuskoEmpty("-")
        binding.labelEmail.text=userData.email.orMuskoEmpty("-")
        binding.labelPhone.text = userData.phoneNumber.orMuskoEmpty("-")


        binding.apply {
            menuAboutUs.build("Tentang Kami", getMuskoDrawable(R.drawable.ic_menu_profile_about_us))
            menuFaq.build("FAQ", getMuskoDrawable(R.drawable.ic_menu_profile_faq))
            menuPrivacyPolicy.build("Privacy Policy", getMuskoDrawable(R.drawable.ic_menu_profile_privacy_policy))

            menuChangePassword.build("Ganti Password", getMuskoDrawable(R.drawable.ic_menu_profile_change_password))
            menuHistoryTransaction.build("Riwayat Transaksi", getMuskoDrawable(R.drawable.ic_menu_profile_history_transaction))
            menuGiveReview.build("Review Saya", getMuskoDrawable(R.drawable.ic_menu_profile_review))
            menuThreadForum.build("Thread Forum", getMuskoDrawable(R.drawable.ic_menu_profile_thread_forum))
        }
    }



    override fun initObserver() {

    }

    private fun showLoading(b: Boolean) {
        if (b) {

        } else {

        }
    }


    private fun findUserCred(userId: Int): Pair<String, String> {
        var userName = ""
        var userCompany = ""
        listUsers.forEachIndexed { index, userResponseItem ->
            if (userId.toString() == userResponseItem.id.toString()) {
                userName = userResponseItem.name
                userCompany = userResponseItem.company.name
            }
        }

        return Pair(userName, userCompany)
    }

    private fun findUserEmail(userId: Int): String {
        var userEmail = ""
        listUsers.forEachIndexed { index, userResponseItem ->
            if (userId.toString() == userResponseItem.id.toString()) {
                userEmail = userResponseItem.email
            }
        }

        return userEmail
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}