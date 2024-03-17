package com.feylabs.halalkan.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.LoginBodyRequest
import com.feylabs.halalkan.data.remote.reqres.auth.LoginResponse
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.databinding.FragmentLoginBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModel()

    override fun initUI() {

        val role = MyPreference(requireContext()).getUserRole();
        if(role?.isNotEmpty() == true){
            when (role) {
                "3" -> {
                    showToast("Anda Login Sebagai Restoran")
                    findNavController().navigate(R.id.action_navigation_loginFragment_to_navigation_initAdminRestoFragment)
                }
                "4"-> {
                    showToast("Anda Login Sebagai Driver")
                    findNavController().navigate(R.id.action_navigation_loginFragment_to_navigation_driverOrderFragment)
                }
                else -> {
                    findNavController().navigate(R.id.action_navigation_loginFragment_to_navigation_newHomeFragment)
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.loginLiveData.observe(viewLifecycleOwner) {
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
                        proceedLogin(userData, token)
                    } ?: run {
                        showSnackbar("Data User Tidak Ditemukan", SnackbarType.ERROR)
                    }
                }
            }
        }
    }

    private fun proceedLogin(userData: UserModel, token: String) {
        val role = userData.rolesId
        muskoPref().saveLoginData(
            userId = userData.id.toString(),
            token = token,
            name = userData.name,
            email = userData.email,
            photo = userData.getPhotoPath(),
            role = role.toString()
        )

        when (role) {
            3 -> {
                findNavController().navigate(R.id.action_navigation_loginFragment_to_navigation_initAdminRestoFragment)
                showSnackbar(getString(R.string.logged_in_message_resto),SnackbarType.SUCCESS)
            }
            4 -> {
                findNavController().navigate(R.id.action_navigation_loginFragment_to_navigation_driverOrderFragment)
                showSnackbar(getString(R.string.logged_in_message_driver),SnackbarType.SUCCESS)
            }
            else -> {
                findNavController().navigate(R.id.action_navigation_loginFragment_to_navigation_newHomeFragment)
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

    override fun initAction() {

        val username = arguments?.getString("username").orEmpty()
        val password = arguments?.getString("password").orEmpty()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(
                LoginBodyRequest(
                    email = username,
                    password = password
                )
            )
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.etUsername.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                binding.etUsername.error = null
            }
        }

        binding.etPassword.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                binding.etPassword.error = null
            }
        }


        binding.btnLogin.setOnClickListener {
            var isError = false;
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty()) {
                isError = true
                binding.etUsername.setError("Username tidak boleh kosong")
            }

            if (password.isEmpty()) {
                isError = true
                binding.etUsername.setError("Password Tidak Boleh Kosong")
            }

            if (isError.not()) {
                viewModel.login(
                    LoginBodyRequest(
                        email = username,
                        password = password
                    )
                )
            }
        }
    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}