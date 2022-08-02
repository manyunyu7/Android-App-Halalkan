package com.feylabs.halalkan.view.prayer.review.create

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
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.customview.imagepreviewcontainer.TypePhotoModel
import com.feylabs.halalkan.customview.imagepreviewcontainer.small.CustomViewImageContainerPreviewSmall
import com.feylabs.halalkan.databinding.FragmentCreateReviewMasjidBinding
import com.feylabs.halalkan.databinding.FragmentReviewNewMasjidBinding
import com.feylabs.halalkan.utils.PermissionUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isGranted
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class MasjidCreateReviewFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentCreateReviewMasjidBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrayerRoomViewModel by viewModel()
    private val PERMISSION_CODE_STORAGE = 1001

    override fun initUI() {
        binding.photoContainer.registerChange(
            object :CustomViewImageContainerPreviewSmall.ListenPhotoChange{
                override fun listen() {
                    if (binding.photoContainer.itemCount()<1){
                        binding.photoContainer.visibility=View.GONE
                    }else{
                        binding.photoContainer.visibility=View.VISIBLE
                    }
                }
            }
        )
    }


    private fun getMasjidId(): String {
        return arguments?.getString("id") ?: ""
    }

    override fun initObserver() {
    }

    override fun initAction() {
        listOf(binding.iconPickPhoto, binding.tvPickPhoto,binding.containerPhoto).forEachIndexed { index, view ->
            view.setOnClickListener {
                askPhotoPermission()
            }
        }
    }

    private fun pickPhoto() {
        val config = GalleryConfig.Build()
            .limitPickPhoto(5)
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

    override fun initData() {
        viewModel.getMasjidReview(getMasjidId(), page = 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateReviewMasjidBinding.inflate(inflater)
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
                    binding.photoContainer.visibility=View.VISIBLE
                    binding.photoContainer.replaceAllImage(tempList)
                }
            }
        }
    }


}