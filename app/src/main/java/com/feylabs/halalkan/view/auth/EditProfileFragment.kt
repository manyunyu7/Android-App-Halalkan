package com.feylabs.halalkan.view.auth

import android.Manifest
import android.accounts.AccountManager
import android.content.Context.ACCOUNT_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.MyProfileResponse
import com.feylabs.halalkan.databinding.FragmentEditProfileBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.base.BaseFragment
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class EditProfileFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    val viewModel: AuthViewModel by viewModel()

    var coverPhoto: File? = null

    override fun initUI() {
        initDataFromSession()
    }

    private fun initDataFromSession() {
        val user = muskoPref().getUserData()
        user?.let {
            binding.tvNameBig.text = user.name
            binding.etEmail.setText(it.email)
            binding.etPhoneNumber.setText(it.phoneNumber)
        }
    }

    private fun checkIfFormChanges() {
        val email = muskoPref().getUserData().email
        val phone = muskoPref().getUserData().phoneNumber
        val name = muskoPref().getUserData().name

        var isChanged = false

        if (coverPhoto != null) {
            isChanged = true
        }

        if (email == binding.etEmail.text.toString())
            isChanged = true

        if (name == binding.etName.text.toString())
            isChanged = true

        if (phone == binding.etPhoneNumber.text.toString())
            isChanged = true

        binding.btnSave.isEnabled = isChanged

    }

    override fun initObserver() {

        viewModel.editProfileLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    DialogUtils.showErrorDialog(
                        context = requireContext(),
                        title = getString(R.string.title_error),
                        message = it.message.toString(),
                        positiveAction = Pair("OK") {

                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    viewModel.getUserProfile()
                    it.data?.let {
                        if (it.success) {
                            DialogUtils.showSuccessDialog(
                                context = requireContext(),
                                title = getString(R.string.title_success),
                                message = getString(R.string.message_data_updated_succesfully),
                                positiveAction = Pair("OK") {
                                },
                                autoDismiss = true,
                                buttonAllCaps = false
                            )
                        } else {
                            DialogUtils.showErrorDialog(
                                context = requireContext(),
                                title = getString(R.string.title_error),
                                message = it.message.toString(),
                                positiveAction = Pair("OK") {
                                },
                                autoDismiss = true,
                                buttonAllCaps = false
                            )
                        }
                    }
                }
            }
        }
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
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
        binding.pageTitle.text = userModel.name
        binding.etName.setText(userModel.name)
        binding.etEmail.setText(userModel.email)
        binding.etPhoneNumber.setText(userModel.phoneNumber)
        binding.ivProfilePicture.loadImageFromURL(requireContext(), userModel.imgFullPath)
        muskoPref().saveUserData(userModel)
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }


    private fun pickPhoto() {
        val config = GalleryConfig.Build()
            .limitPickPhoto(5)
            .singlePhoto(false)
            .build()

        RazkyGalleryActivity.openActivityFromFragment(this, 120, config)
    }

    private fun askPhotoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissions(permission, PERMISSION_CODE_STORAGE)
            } else {
                pickPhoto()
            }
        } else {
            pickPhoto()
        }
    }


    override fun initAction() {
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 222)

        binding.btnChoosePhoto.setOnClickListener {
            askPhotoPermission()
        }

        binding.etEmail.doOnTextChanged { text, start, before, count ->
            checkIfFormChanges()
        }
        binding.etPhoneNumber.doOnTextChanged { text, start, before, count ->
            checkIfFormChanges()
        }
        binding.etName.doOnTextChanged { text, start, before, count ->
            checkIfFormChanges()
        }

        binding.btnSave.setOnClickListener {
            var isError = false
            var email = binding.etEmail.text.toString()
            var phone = binding.etPhoneNumber.text.toString()
            var name = binding.etName.text.toString()

            if (email.isEmpty()) {
                isError = true
                binding.etEmail.setError(getString(R.string.required_column))
            }
            if (phone.isEmpty()) {
                isError = true
                binding.etPhoneNumber.setError(getString(R.string.required_column))
            }
            if (name.isEmpty()) {
                isError = true
                binding.etName.setError(getString(R.string.required_column))
            }
            if (phone.length < 9) {
                isError = true
                binding.etPhoneNumber.setError(getString(R.string.error_message_min_is_9))
            }

            if (isError.not()) {
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_data_will_updated),
                    positiveAction = Pair("OK") {
                        viewModel.updateProfile(
                            name = name, phone = phone,
                            email = email, coverPhoto
                        )
                    },
                    negativeAction = Pair(
                        "No",
                        { }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }
        }
    }

    override fun initData() {
        viewModel.getUserProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    permission
                )
            ) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        } else {
            showToast("Permission is Granted")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults!!)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permissions[0]!!
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (requestCode == 222) {
                askPhotoPermission()
                showToast("Permission Granted")
            }
        } else {
            showToast("Permission is not granted")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //list of photos of seleced
        checkIfFormChanges()
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

                        binding.ivProfilePicture.loadImage(
                            requireContext(),
                            file = uploadedFile
                        )
                    }
                }
            }
        }
    }


}