package com.feylabs.halalkan.view.auth

import android.Manifest
import android.accounts.AccountManager
import android.content.Context.ACCOUNT_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
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
import com.feylabs.halalkan.databinding.FragmentEditProfileBinding
import com.feylabs.halalkan.utils.base.BaseFragment


class EditProfileFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

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

        if (email == binding.etEmail.text.toString())
            isChanged = true

        if (name == binding.etName.text.toString())
            isChanged = true

        if (phone == binding.etPhoneNumber.text.toString())
            isChanged = true

        binding.btnSave.isEnabled = isChanged

    }

    override fun initObserver() {
    }

    override fun initAction() {
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 222);

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

            }
        }
    }

    override fun initData() {
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
                showToast("Permission Granted")
            }
        } else {
            showToast("Permission is not granted")
        }
    }


}