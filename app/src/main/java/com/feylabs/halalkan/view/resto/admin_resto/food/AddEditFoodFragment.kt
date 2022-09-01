package com.feylabs.halalkan.view.resto.admin_resto.food

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentAddEditRestoFoodBinding
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.tangxiaolv.telegramgallery.GalleryActivity
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class AddEditFoodFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentAddEditRestoFoodBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()
    private val PERMISSION_CODE_STORAGE = 1001

    private var mapCert = mutableMapOf<String, String>()
    private var mapFoodType = mutableMapOf<String, String>()

    var coverPhoto: File? = null


    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.getDetailResto(getRestoId())
        viewModel.myRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {}
                is Success -> {
                    setupRestoData(it.data)
                }
            }
        }

        viewModel.foodCategoryLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                }
                is Success -> {
                    val spinnerArray: MutableList<String> = mutableListOf()
                    it.data?.forEachIndexed { index, data ->
                        spinnerArray.add(data.name)
                        mapFoodType.put(data.name, data.id.toString())
                    }
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireContext(), android.R.layout.simple_spinner_item, spinnerArray
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerTypeFood.adapter = adapter
                }
            }
        }

        viewModel.certLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                }
                is Success -> {
                    val spinnerArray: MutableList<String> = mutableListOf()
                    it.data?.forEachIndexed { index, restaurantCertificationItem ->
                        spinnerArray.add(restaurantCertificationItem.name)
                        mapCert.put(
                            restaurantCertificationItem.name,
                            restaurantCertificationItem.id.toString()
                        )
                    }

                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireContext(), android.R.layout.simple_spinner_item, spinnerArray
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerGeneralCategory.adapter = adapter

                }
            }
        }

        viewModel.createFoodLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {}
                is Success -> {
                    it.data?.let {
                    }
                }
            }
        }
    }



    private fun getScreenType(): String {
        val type = arguments?.getString("type") ?: ""
        return type
    }

    private fun getRestoId(): String {
        val type = arguments?.getString("id") ?: ""
        return type
    }

    override fun initAction() {
        binding.apply {
            listOf(etName,etDesc,).forEachIndexed { index, editText ->
                editText.addTextChangedListener {
                    if(it.toString().isNotEmpty()){
                        editText.error=null
                    }
                }
            }
        }
        binding.btnRegister.setOnClickListener {
            var message = ""
            var isError = false

            val name = binding.etName.text.toString()
            val desc = binding.etDesc.text.toString()
            val price = binding.etPrice.text.toString()
            val category = mapCert[binding.spinnerGeneralCategory.selectedItem]?.toIntOrNull() ?: -99
            val foodType = mapFoodType[binding.spinnerTypeFood.selectedItem]?.toIntOrNull() ?: -99

            if (name.isEmpty()){
                isError=true
                binding.etName.error=getString(R.string.required_column)
            }

            if (desc.isBlank()){
                isError=true
                binding.etDesc.error=getString(R.string.required_column)
            }

            if (coverPhoto==null){
                isError=true
                showSnackbar(getString(R.string.message_photo_is_require),SnackbarType.ERROR)
            }

            if (isError.not() && coverPhoto!=null){
                viewModel.createFood(
                    typeFoodId = foodType,
                    categoryId = category,
                    restoran_id = getChoosenResto().toIntOrNull(),
                    description = desc,
                    name = name,
                    price =  price.toIntOrNull()?:0,
                    coverPhoto!!
                )
            }

        }

        binding.includePhotoPreview.apply {
            btnDelete.makeGone()
            btnEdit.makeVisible()

            btnEdit.setOnClickListener {
                askPhotoPermission()
            }

            btnDelete.setOnClickListener {
                coverPhoto=null
                binding.includePhotoPreview.photo.loadImage(
                    requireContext(), R.drawable.bg_header_daylight
                )
                btnEdit.makeVisible()
                btnDelete.makeGone()
            }
        }

    }

    override fun initData() {
        viewModel.getRestoCert()
        viewModel.getFoodCategoryOnResto(getChoosenResto())
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
        _binding = FragmentAddEditRestoFoodBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRestoData(data: AllRestoNoPagination?) {

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

                        binding.includePhotoPreview.btnEdit.makeGone()
                        binding.includePhotoPreview.btnDelete.makeVisible()

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