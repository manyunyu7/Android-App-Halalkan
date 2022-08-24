package com.feylabs.halalkan.view.resto.admin_resto.driver

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.databinding.FragmentAddEditCategoryBinding
import com.feylabs.halalkan.databinding.FragmentRegisterBinding
import com.feylabs.halalkan.databinding.FragmentXrestoAddDriverBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel


class AddEditDriverFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoAddDriverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DriverViewModel by viewModel()

    override fun initUI() {}

    override fun initObserver() {
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
                    it.data
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

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }

    override fun initAction() {

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.apply {
            val view = arrayListOf(etName, etPhone, etPassword, etUsername)
            view.forEachIndexed { index, editText ->
                editText.doOnTextChanged { text, start, before, count ->
                    if (text?.isNotEmpty() == true) {
                        editText.error = null
                    }
                }
            }
        }
    }

    override fun initData() {
        binding.btnRegister.setOnClickListener {
            var isError = false;
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val phone = binding.etPhone.text.toString()
            val name = binding.etName.text.toString()

            if (username.isEmpty()) {
                isError = true
                binding.etUsername.setError("Username tidak boleh kosong")
            }

            if (phone.isEmpty()) {
                isError = true
                binding.etPhone.setError("Nomor Telepon Diperlukan")
            }

            if (name.isEmpty()) {
                isError = true
                binding.etName.setError("Nomor Telepon Diperlukan")
            }

            if (password.isEmpty()) {
                isError = true
                binding.etPassword.setError("Password Tidak Boleh Kosong")
            }


            if (isError.not()) {
                viewModel.addNewDriver(
                    RegisterBodyRequest(
                        password = password,
                        confirmPassword = password,
                        rolesId = 4,
                        email = username,
                        phoneNumber = phone,
                        name = name
                    )
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoAddDriverBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}