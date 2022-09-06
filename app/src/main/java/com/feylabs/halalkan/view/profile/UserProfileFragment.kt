package com.feylabs.halalkan.view.profile

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
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
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.auth.MyProfileResponse
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.databinding.FragmentUserProfileBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.tangxiaolv.telegramgallery.GalleryActivity
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File

class UserProfileFragment : BaseFragment() {

    val viewModel: AuthViewModel by viewModel()
    private var _binding: FragmentUserProfileBinding? = null

    // save temporary user list
    private var listUsers = mutableListOf<UserResponse.UserResponseItem>()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var coverPhoto: File? = null

    override fun initAction() {
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.navigation_editProfileFragment)
        }
        binding.btnLogout.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_logged_out),
                positiveAction = Pair("OK") {
                    muskoPref().clearPreferences()
                    findNavController().navigate(R.id.navigation_newHomeFragment)
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }

    override fun initData() {
        viewModel.getUserProfile()
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

        val userData = muskoPref().getUserData()
        binding.ivMainImage.loadImageFromURL(requireContext(), userData.getPhotoPath())
        binding.labelInfoProfileUserName.text = userData.name.orMuskoEmpty("-")
        binding.labelEmail.text = userData.email.orMuskoEmpty("-")
        binding.labelPhone.text = userData.phoneNumber.orMuskoEmpty("-")


        binding.apply {
            menuAboutUs.build(
                getString(R.string.title_profile_about_us),
                getMuskoDrawable(R.drawable.ic_menu_profile_about_us)
            )
            menuFaq.build("FAQ", getMuskoDrawable(R.drawable.ic_menu_profile_faq))
            menuPrivacyPolicy.build(
                getString(R.string.title_profile_privacy_policy),
                getMuskoDrawable(R.drawable.ic_menu_profile_privacy_policy)
            )

            menuChangePassword.build(
                getString(R.string.title_profile_change_password),
                getMuskoDrawable(R.drawable.ic_menu_profile_change_password)
            )
            menuHistoryTransaction.build(
                getString(R.string.title_profile_transaction_history),
                getMuskoDrawable(R.drawable.ic_menu_profile_history_transaction)
            )
            menuGiveReview.build(
                getString(R.string.title_profile_my_review),
                getMuskoDrawable(R.drawable.ic_menu_profile_review)
            )
            menuThreadForum.build(
                getString(R.string.title_profile_forum_thread),
                getMuskoDrawable(R.drawable.ic_menu_profile_thread_forum)
            )

            menuChangePassword.setOnClickListener {
                findNavController().navigate(R.id.navigation_userChangePasswordFragment)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //list of photos of seleced
        if (data != null) {
            if (requestCode == 120) {
                val photos = data.getSerializableExtra(GalleryActivity.PHOTOS) as List<String>
                if (photos != null) {
                    //Clear adapter data
                    val tempList = mutableListOf<CustomViewPhotoModel>()
                    photos.forEachIndexed { index, s ->
                        Timber.d("photo loop @${index}")
                        Timber.d("photo loop @${photos[index]}")
                        val uriFIle = "file://" + photos[index]
                        val mFileUri = Uri.parse(uriFIle) //for glide
                        val uploadedFile = File(Uri.parse(uriFIle).path.toString())
                        coverPhoto = uploadedFile

                        binding.ivMainImage.loadImage(
                            requireContext(),
                            file = uploadedFile
                        )
                    }
                }
            }
        }
    }


    override fun initObserver() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showLoading(false)
                }
                is Error -> {
                    showLoading(false)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        setupUserData(it)
                    }
                }
            }
        }
    }

    private fun setupUserData(it: MyProfileResponse) {
        val userModel = it.userModel
        binding.labelEmail.text = userModel.name
        binding.labelPhone.text = userModel.phoneNumber
        binding.ivMainImage.loadImageFromURL(requireContext(), userModel.imgFullPath)

        muskoPref().saveUserData(userModel)
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
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