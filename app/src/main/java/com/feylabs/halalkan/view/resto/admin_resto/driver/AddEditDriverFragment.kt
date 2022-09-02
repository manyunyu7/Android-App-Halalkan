package com.feylabs.halalkan.view.resto.admin_resto.driver

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetOrderNotes
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetResetUserPassword
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.databinding.FragmentXrestoAddEditDriverBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class AddEditDriverFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoAddEditDriverBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    private val viewModel: DriverViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private val restoViewModel: AdminRestoViewModel by viewModel()

    var coverPhoto: File? = null


    override fun initUI() {
        if (isEdit()) {
            binding.labelPassword.makeGone()
            binding.containerEditDriver.makeVisible()
            binding.infoRegister.makeGone()
            binding.etPassword.makeGone()
        } else {
        }
    }

    override fun initObserver() {

        authViewModel.userProfileLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(false)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it?.data?.let {
                        setupUserDataFromNetwork(it)
                    }
                }
            }
        }
        viewModel.updateDriverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_updated_succesfully),
                        positiveAction = Pair("OK") {
                            findNavController().popBackStack()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }
        viewModel.addNewDriverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    val token = it.data?.accessToken.orEmpty()
                    it.data?.user?.let { userData ->
                        showSnackbar(it.message.toString(), SnackbarType.SUCCESS)
                        showLoading(false)
                        proceedRegister(userData, token)
                    } ?: run {
                        showSnackbar("Data User Tidak Ditemukan", SnackbarType.ERROR)
                    }
                }
            }
        }
        viewModel.deleteDriverLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getString(R.string.message_data_created_succesfully),
                        positiveAction = Pair("OK") {
                            findNavController().navigateUp()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }

    }

    private fun setupUserDataFromNetwork(it: UserModel) {
        binding.etName.setText(it.name)
        binding.etPhone.setText(it.phoneNumber)
        binding.etUsername.setText(it.email)
        binding.etPassword.makeGone()
        binding.includePhotoPreview.photo.loadImageFromURL(requireContext(), it.imgFullPath)
    }

    private fun proceedRegister(userData: UserModel, token: String) {
        DialogUtils.showSuccessDialog(
            context = requireContext(),
            title = getString(R.string.title_success),
            message = getString(R.string.message_data_created_succesfully),
            positiveAction = Pair("OK") {
                findNavController().popBackStack()
            },
            autoDismiss = true,
            buttonAllCaps = false
        )
    }

    private fun getUserId(): String {
        return arguments?.getString("driverId").orEmpty()
    }

    private fun isEdit(): Boolean {
        return getUserId() != ""
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }

    override fun initAction() {

        binding.btnDeleteDriver.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_data_will_deleted),
                positiveAction = Pair("OK") {
                    viewModel.deleteDriver(
                        driverId = getUserId(),
                    )
                },
                negativeAction = Pair(
                    "No",
                    { }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        binding.btnResetPassword.setOnClickListener {
            showBottomSheetResetPassword()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            val view = arrayListOf(etName, etPhone, etUsername, etPassword.editText)
            view.forEachIndexed { index, editText ->
                editText?.doOnTextChanged { text, start, before, count ->
                    if (text?.isNotEmpty() == true) {
                        editText.error = null
                    }
                }
            }
        }

        binding.includePhotoPreview.apply {
            btnDeletePhoto.makeGone()
            btnEditPhoto.makeVisible()

            btnEditPhoto.setOnClickListener {
                askPhotoPermission()
            }

            btnDeletePhoto.setOnClickListener {
                coverPhoto = null
                binding.includePhotoPreview.photo.loadImage(
                    requireContext(), R.drawable.bg_header_daylight
                )
                btnEditPhoto.makeVisible()
                btnDeletePhoto.makeGone()
            }
        }

        binding.btnRegister.setOnClickListener {
            var isError = false;
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.editText?.text.toString()
            val phone = binding.etPhone.text.toString()
            val name = binding.etName.text.toString()

            if (username.isEmpty()) {
                isError = true
                binding.etUsername.setError(getString(R.string.required_column))
            }

            if (phone.isEmpty()) {
                isError = true
                binding.etPhone.setError(getString(R.string.required_column))
            }

            if (name.isEmpty()) {
                isError = true
                binding.etName.setError(getString(R.string.required_column))
            }


            if (isEdit().not())
                if (password.isEmpty()) {
                    isError = true
                    binding.etPassword.setError(getString(R.string.required_column))
                }

            if (isError.not()) {
                if (isEdit()) {
                    DialogUtils.showConfirmationDialog(
                        context = requireContext(),
                        title = getString(R.string.label_are_you_sure),
                        message = getString(R.string.message_data_will_updated),
                        positiveAction = Pair("OK") {
                            showToast(coverPhoto.toString())
                            viewModel.updateDriver(
                                driverId = getUserId(),
                                driverName = name,
                                email = username,
                                phone = phone,
                                file = coverPhoto
                            )
                        },
                        negativeAction = Pair(
                            "No",
                            { }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                } else {
                    DialogUtils.showConfirmationDialog(
                        context = requireContext(),
                        title = getString(R.string.label_are_you_sure),
                        message = getString(R.string.message_data_will_uploaded),
                        positiveAction = Pair("OK") {
                            viewModel.addNewDriver(
                                coverPhoto,
                                RegisterBodyRequest(
                                    password = password,
                                    confirmPassword = password,
                                    rolesId = 4,
                                    email = username,
                                    phoneNumber = phone,
                                    name = name
                                )
                            )
                        },
                        negativeAction = Pair(
                            getString(R.string.title_no),
                            { }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            } else {
                showToast(getString(R.string.message_please_check_input))
            }
        }
    }

    private fun showBottomSheetResetPassword() {
        BottomSheetResetUserPassword.instance(
            userId = getUserId()
        ).show(getMFragmentManager(), BottomSheetOrderNotes().tag)
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

    private fun pickPhoto() {
        val config = GalleryConfig.Build()
            .limitPickPhoto(5)
            .singlePhoto(false)
            .build()

        RazkyGalleryActivity.openActivityFromFragment(this, 120, config)
    }

    override fun initData() {
        if (isEdit()) {
            authViewModel.getUserProfile(getUserId())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoAddEditDriverBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

                        binding.includePhotoPreview.btnEditPhoto.makeGone()
                        binding.includePhotoPreview.btnDeletePhoto.makeVisible()

                        binding.includePhotoPreview.photo.loadImage(
                            requireContext(),
                            file = uploadedFile
                        )
                    }
                }
            }
        }
    }


}