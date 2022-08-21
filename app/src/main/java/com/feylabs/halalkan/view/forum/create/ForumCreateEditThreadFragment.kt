package com.feylabs.halalkan.view.forum.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.customview.imagepreviewcontainer.TypePhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.forum.ForumCategoryResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumDetailResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentForumCreateThreadBinding
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.PermissionUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isGranted
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.forum.ForumViewModel
import com.google.android.material.chip.Chip
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class ForumCreateEditThreadFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentForumCreateThreadBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<ForumViewModel>()

    private val PERMISSION_CODE_STORAGE = 1001
    var currentCategory = ""

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.detailForumLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    it.data?.let {
                        setupForumData(it)
                        viewModel.getForumCategory()
                    }
                }
            }
        }

        viewModel.forumCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        setupCategoryChip(it)
                    }
                }
            }
        }

        viewModel.updateForumLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        findNavController().navigateUp()
                        showSnackbar("Forum Updated Successfully")
                    }
                }
            }
        }
        viewModel.createForumLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        findNavController().navigateUp()
                        showSnackbar("Forum Created Successfully")
                    }
                }
            }
        }
    }

    private fun setupForumData(it: ForumDetailResponse) {
        binding.etDesc.editText?.setText(it.forum.body)
        binding.etTitle.editText?.setText(it.forum.title)

        currentCategory = it.forum.categoryId.toString()

        binding.photoContainer.makeVisible()

        if (it.forum.img == null) {
            binding.photoContainer.makeGone()
        }

        binding.photoContainer.replaceAllImage(
            mutableListOf(
                CustomViewPhotoModel(
                    url = it.forum.img_full_path,
                    type = TypePhotoModel.URL,
                    isDeletable = true
                )
            )
        )
    }

    private fun getSelectedCategoryId(): String {
        val ids: List<Int> = binding.chipGroupCategory.getCheckedChipIds()
        var selected = "-99"
        ids.forEachIndexed { index, i ->
            val chip = view?.findViewById<Chip>(i)
            selected = chip?.tag.toString()
        }
        return selected
    }

    private fun setupCategoryChip(data: ForumCategoryResponse) {
        data.forEachIndexed { index, forumCategoryResponseItem ->
            val chip = Chip(requireContext())
            chip.text = forumCategoryResponseItem.name
            chip.tag = forumCategoryResponseItem.id
            chip.isCheckable = true


            if (forumCategoryResponseItem.id.toString() == currentCategory) {
                chip.isChecked = true
            }

            chip.setTextColor(resources.getColor(R.color.white))
            chip.chipStrokeColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_stroke_color
                    )
                )
            chip.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_text_color
                    )
                )
            )
            chip.chipBackgroundColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_color
                    )
                )
            binding.chipGroupCategory.addView(chip)
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.includeLoading.root.makeVisible()
        } else {
            binding.includeLoading.root.makeGone()
        }
    }

    override fun initAction() {

        binding.containerPhoto.setOnClickListener {
            askPhotoPermission()
        }

        binding.btnSave.setOnClickListener {
            var showError = false
            val title = binding.etTitle.editText?.text
            val desc = binding.etDesc.editText?.text
            var uploadedFile: File? = null

            binding.photoContainer.adapter.data.forEachIndexed { index, customViewPhotoModel ->
                uploadedFile = customViewPhotoModel.file
            }

            if (title?.isEmpty() == true) {
                showError = true
                showSnackbar("Please Check Your Input")
            }

            if (getSelectedCategoryId() == "-99") {
                showError = true
                showSnackbar("Choose Category")
            }

            if (showError.not()) {

                var message = "Your Question will be posted to the forum"
                if (isEdit()) message = "Your question will be updated"

                var isDeletingImage = binding.photoContainer.itemCount() == 0

                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = "Are You Sure",
                    message = message,
                    positiveAction = Pair("OK") {
                        if (isEdit()) {
                            viewModel.updateThread(
                                id = getForumId().toString(),
                                title = title.toString(),
                                desc = desc.toString(),
                                file = uploadedFile,
                                category = getSelectedCategoryId(),
                                isDeletingImage = isDeletingImage
                            )
                        } else {
                            viewModel.createThread(
                                title = title.toString(),
                                desc = desc.toString(),
                                file = uploadedFile,
                                category = getSelectedCategoryId()
                            )

                        }

                    },
                    negativeAction = Pair(
                        "No",
                        { }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
            }

        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun getForumId(): Int {
        val obj = arguments?.getString("forumId") ?: ""
        return obj.toIntOrNull() ?: -99
    }

    fun isEdit(): Boolean {
        return getForumId() != -99
    }

    override fun initData() {
        if (isEdit()) {
            viewModel.getForumDetail(getForumId())
        } else {
            viewModel.getForumCategory()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumCreateThreadBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickPhoto() {
        val config = GalleryConfig.Build()
            .limitPickPhoto(1)
            .singlePhoto(false)
            .build()

        RazkyGalleryActivity.openActivityFromFragment(this, 120, config)
    }

    private fun checkPhotoPemission(): Boolean {
        val permReadExternal = PermissionUtil.getPermissionStatus(
            requireActivity(),
            PermissionUtil.PER_READ_EX_STORAGE
        )
        return permReadExternal.isGranted()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //list of photos of seleced
        if (data != null) {
            showToast(data.toString())
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
                        tempList.add(
                            CustomViewPhotoModel(
                                uri = uriFIle,
                                isDeletable = true,
                                uriGlide = mFileUri,
                                file = uploadedFile,
                                type = TypePhotoModel.TELEGRAM_PHOTO_PICKER
                            )
                        )
                    }
                    binding.photoContainer.visibility = View.VISIBLE

                    //if there is photo exist, then remove first
                    if (binding.photoContainer.itemCount() > 0) {
                        binding.photoContainer.clearData()
                    }

                    tempList.forEachIndexed { index, customViewPhotoModel ->
                        binding.photoContainer.addNewImage(customViewPhotoModel)
                    }
                }
            }
        }
    }


}