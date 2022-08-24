package com.feylabs.halalkan.view.resto.admin_resto.driver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.databinding.FragmentAddEditCategoryBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel


class AddEditDriverFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentAddEditCategoryBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()
    private val PERMISSION_CODE_STORAGE = 1001


    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.createRestoFoodCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar("Berhasil Menyimpan Kategori", SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    showSnackbar("Berhasil Menyimpan Kategori", SnackbarType.SUCCESS)
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun getScreenType(): String {
        val type = arguments?.getString("type") ?: ""
        return type
    }

    private fun getRestoId(): String {
        return getChoosenResto()
    }

    override fun initAction() {
        binding.apply {
            listOf(etName).forEachIndexed { index, editText ->
                editText.addTextChangedListener {
                    if (it.toString().isNotEmpty()) {
                        editText.error = null
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            var isError = false
            showToast("Menyimpan Perubahan")
            val name = binding.etName.text.toString()

            if (name.isNotEmpty().not()) {
                isError = true
                binding.etName.error = getString(R.string.required_column)
            }

            if (isError.not()) {
                viewModel.addFoodCategoryForResto(
                    name = name, getRestoId()
                )
            }

        }


    }

    override fun initData() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEditCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }


}