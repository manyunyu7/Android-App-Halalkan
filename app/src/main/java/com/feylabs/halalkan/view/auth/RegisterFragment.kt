package com.feylabs.halalkan.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.RegisterBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.databinding.FragmentRegisterBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import org.koin.android.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModel()


    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.registerLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                    viewModel.resetLogin()
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    viewModel.resetLogin()
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
        showSnackbar("Registrasi Berhasil, Silakan Login menggunakan Akun Anda",SnackbarType.SUCCESS)
        findNavController().navigate(R.id.action_navigation_registerFragment_to_navigation_loginFragment, bundleOf(
            "username" to userData.email,
            "password" to binding.etPassword.text.toString()
        ))
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim.makeVisible()
        } else {
            binding.loadingAnim.makeGone()
        }
    }

    override fun initAction() {

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.navigation_loginFragment)
        }

        binding.apply {
            val view = arrayListOf<EditText>(etName, etPhone, etPassword, etUsername)
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

            var roles = 2
            val spinnerValue = binding.spinnerJenisPemohon.selectedItemPosition
            if (spinnerValue==0)
                roles = 2
            if (spinnerValue==1){
                roles=3
            }

            if (isError.not()) {
                viewModel.register(
                    RegisterBodyRequest(
                        password = password,
                        confirmPassword = password,
                        rolesId = roles,
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
        _binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}