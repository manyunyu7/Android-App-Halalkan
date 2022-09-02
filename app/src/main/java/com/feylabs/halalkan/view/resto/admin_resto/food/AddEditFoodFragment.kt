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
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.food.FoodModelResponse
import com.feylabs.halalkan.databinding.FragmentAddEditRestoFoodBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImage
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
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

    private var mapFoodGlobalType = mutableMapOf<String, Int>()
    private var mapFoodCategory = mutableMapOf<String, Int>()

    var coverPhoto: File? = null


    override fun initUI() {
        if (isEdit()){
            binding.btnDeleteFood.makeVisible()
        }
    }

    override fun initObserver() {

        viewModel.foodDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                }
                is Error -> {
                    showSnackbar(it.message.toString(),SnackbarType.ERROR)
                    showLoading(false)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    it.data?.let {
                        setupFoodData(it)
                    }
                    showLoading(false)
                }
            }
        }

        viewModel.foodCategoryLiveData.observe(viewLifecycleOwner) {
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
                    if (isEdit()) {
                        viewModel.getFoodDetail(getFoodId())
                    }
                    showLoading(false)
                    val spinnerArray: MutableList<String> = mutableListOf()
                    it.data?.forEachIndexed { index, data ->
                        spinnerArray.add(data.name)
                        mapFoodCategory.put(data.name, data.id)
                    }
                    val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                        requireContext(), android.R.layout.simple_spinner_item, spinnerArray
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerRestaurantFoodCategory.adapter = adapter
                }
            }
        }

        viewModel.foodTypeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                }
                is Success -> {
                    //load next data
                    viewModel.getFoodCategoryOnResto(getChoosenResto())


                    val spinnerArray: MutableList<String> = mutableListOf()
                    it.data?.forEachIndexed { index, foodType ->
                        spinnerArray.add(foodType.name)
                        mapFoodGlobalType.put(
                            foodType.name,
                            foodType.id
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

        viewModel.createUpdateFoodLiveData.observe(viewLifecycleOwner) {
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
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = getString(R.string.title_success),
                        message = getSuccessMessage(),
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

    private fun getSuccessMessage(): String {
        return if (isEdit()) {
            getString(R.string.message_data_updated_succesfully)
        } else {
            getString(R.string.message_data_created_succesfully)
        }
    }

    private fun setupFoodData(data: FoodModelResponse) {
        binding.etName.setText(data.name)
        binding.etDesc.setText(data.description)
        binding.etPrice.setText(data.price.toInt().toString())

        binding.includePhotoPreview.photo.loadImageFromURL(
            requireContext(), data.imgFullPath
        )


        val adapterSpinnerGeneral = binding.spinnerGeneralCategory.adapter
        for (i in 0 until adapterSpinnerGeneral.count) {
            if (adapterSpinnerGeneral.getItem(i).toString()==data.typeFoodName){
                binding.spinnerGeneralCategory.setSelection(i)
            }
        }


        val adapterSpinnerFoodCategory = binding.spinnerRestaurantFoodCategory.adapter
        for (i in 0 until adapterSpinnerFoodCategory.count) {
            if (adapterSpinnerFoodCategory.getItem(i).toString()==data.categoryName){
                binding.spinnerRestaurantFoodCategory.setSelection(i)
            }
        }




    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }

    private fun isEdit(): Boolean {
        return (getFoodId() == "").not()
    }

    private fun getFoodId(): String {
        val foodId = arguments?.getString("foodId").orMuskoEmpty("")
        return foodId
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
        binding.btnDeleteFood.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_data_will_deleted),
                positiveAction = Pair("OK") {
                    viewModel.deleteFood(
                        foodId = getFoodId(),
                    )
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        binding.includePhotoPreview.apply {
            btnDelete.makeGone()
            btnEdit.makeVisible()

            btnEdit.setOnClickListener {
                askPhotoPermission()
            }

            btnDelete.setOnClickListener {
                coverPhoto = null
                binding.includePhotoPreview.photo.loadImage(
                    requireContext(), R.drawable.bg_header_daylight
                )
                btnEdit.makeVisible()
                btnDelete.makeGone()
            }
        }

        binding.apply {
            listOf(etName, etDesc).forEachIndexed { index, editText ->
                editText.addTextChangedListener {
                    if (it.toString().isNotEmpty()) {
                        editText.error = null
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            var isError = false

            val name = binding.etName.text.toString()
            val desc = binding.etDesc.text.toString()
            val price = binding.etPrice.text.toString()
            val category = mapFoodCategory[binding.spinnerRestaurantFoodCategory.selectedItem] ?: -99
            val foodType = mapFoodGlobalType[binding.spinnerGeneralCategory.selectedItem] ?: -99

            if (name.isEmpty()) {
                isError = true
                binding.etName.error = getString(R.string.required_column)
            }

            if (desc.isBlank()) {
                isError = true
                binding.etDesc.error = getString(R.string.required_column)
            }

            if (coverPhoto == null && isEdit().not()) {
                isError = true
                showSnackbar(getString(R.string.message_photo_is_require), SnackbarType.ERROR)
            }


            if (isError.not()) {
                if (isEdit()) {
                    DialogUtils.showConfirmationDialog(
                        context = requireContext(),
                        title = getString(R.string.label_are_you_sure),
                        message = getString(R.string.message_data_will_updated),
                        positiveAction = Pair("OK") {
                            viewModel.updateFood(
                                foodId = getFoodId(),
                                typeFoodId = foodType,
                                categoryId = category,
                                restoran_id = getChoosenResto().toInt(),
                                description = desc,
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                coverPhoto
                            )
                        },
                        negativeAction = Pair(
                            getString(R.string.title_no),
                            { showToast(getString(R.string.label_canceled)) }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                } else {
                    DialogUtils.showConfirmationDialog(
                        context = requireContext(),
                        title = getString(R.string.label_are_you_sure),
                        message = getString(R.string.message_please_check_your_data),
                        positiveAction = Pair("OK") {
                            viewModel.createFood(
                                typeFoodId = foodType,
                                categoryId = category,
                                restoran_id = getChoosenResto().toIntOrNull(),
                                description = desc,
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                coverPhoto!!
                            )
                        },
                        negativeAction = Pair(
                            getString(R.string.title_no),
                            { showToast(getString(R.string.label_canceled)) }),
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }

        }

    }

    override fun initData() {
        viewModel.getFoodType()
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