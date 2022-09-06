package com.feylabs.halalkan.view.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.databinding.FragmentProfileChangePasswordBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel


class ChangePasswordFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentProfileChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModel()

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.changePasswordLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    DialogUtils.showErrorDialog(
                        context = requireContext(),
                        title = getString(R.string.title_error),
                        message = it.message.toString(),
                        positiveAction = Pair("OK") {

                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
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
                            findNavController().navigateUp()
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }
    }


    private fun showLoading(b: Boolean) {
        if (b) {
            binding.loadingAnim?.makeVisible()
        } else {
            binding.loadingAnim?.makeGone()
        }
    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            var isError = false;
            val currentPassword = binding.etCurrentPassword?.editText?.text.toString()
            val newPassword = binding.etNewPassword?.editText?.text.toString()

            if (currentPassword.length < 7) {
                isError = true
                binding.etCurrentPassword?.error = getString(R.string.message_password_min_6)
            }

            if (newPassword.length < 7) {
                isError = true
                binding.etNewPassword?.error = getString(R.string.message_password_min_6)
            }

            if (isError.not()) {
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = getString(R.string.label_are_you_sure),
                    message = getString(R.string.message_data_will_updated),
                    positiveAction = Pair("OK") {
                        viewModel.changePassword(
                            oldPassword = currentPassword,
                            newPassword = newPassword
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

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileChangePasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}