package com.feylabs.halalkan.view.driver.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.databinding.FragmentCompleteOrderBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.FileUtil.bitmapToFile
import com.feylabs.halalkan.utils.FileUtil.getRealPathFromURI
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.github.gcacace.signaturepad.views.SignaturePad
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File


class CompleteOrderFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentCompleteOrderBinding? = null
    private val binding get() = _binding!!

    val viewModel: OrderViewModel by viewModel()
    val driverViewModel: DriverViewModel by viewModel()

    override fun initUI() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun initObserver() {
        viewModel.finishOrderLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let {
                        DialogUtils.showSuccessDialog(
                            context = requireContext(),
                            title = getString(R.string.title_success),
                            message = getString(R.string.message_order_completed_successfully),
                            positiveAction = Pair("OK") {
                                findNavController().popBackStack(
                                    R.id.navigation_driverOrderFragment,
                                    true
                                )
                            },
                            autoDismiss = true,
                            buttonAllCaps = false
                        )
                    }
                }
            }
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
        binding.btnClear.setOnClickListener {
            binding.signaturePad.clear()
        }

        binding.signaturePad.setOnSignedListener(object :SignaturePad.OnSignedListener{
            override fun onStartSigning() {
                binding.tvLabelSignHere.makeGone()
            }

            override fun onSigned() {

            }

            override fun onClear() {
                binding.tvLabelSignHere.makeVisible()
            }

        })

        binding.etRecipientName.editText?.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty()) binding.etRecipientName.error = null
        }

        binding.btnNext.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_order_will_finish),
                positiveAction = Pair("OK") {

                    var isError = false

                    if (binding.signaturePad.isEmpty) {
                        isError = true
                        showSnackbar(getString(R.string.message_please_fill_the_signature_first))
                    }

                    if (binding.etRecipientName.editText?.text.toString().isEmpty()) {
                        binding.etRecipientName.error = getString(R.string.required_column)
                        isError = true
                    }

                    if (isError.not()) {
                        val file = File(
                            getRealPathFromURI(
                                requireActivity(),
                                bitmapToFile(
                                    requireActivity(),
                                    binding.signaturePad.signatureBitmap
                                )
                            ).toString()
                        )

                        val recipientName = binding.etRecipientName.editText?.text.toString()
                        viewModel.finishOrder(
                            orderId = getOrderId(),
                            recipientName = recipientName,
                            file
                        )
                    }
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }

    private fun getOrderId(): String {
        return arguments?.getString("orderId").orMuskoEmpty("")
    }

    override fun initData() {
        driverViewModel.getDriverOrder()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompleteOrderBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}