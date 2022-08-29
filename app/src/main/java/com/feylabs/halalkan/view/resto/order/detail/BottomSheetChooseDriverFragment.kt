package com.feylabs.halalkan.view.resto.order.detail

import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.utils.CommonUtil.showSnackbar
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.admin_resto.driver.ManageDriverAdapter
import com.feylabs.halalkan.databinding.LayoutBottomsheetChooseDriverBinding as MainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class BottomSheetChooseDriverFragment : BottomSheetDialogFragment() {

    private lateinit var selectedAction: (selectedDriver: String) -> Unit

    private var restoId: String = ""
    private var type = ""

    val viewModel by viewModel<DriverViewModel>()
    private val mAdapter by lazy { ManageDriverAdapter() }

    companion object {
        private val TAG = "BOTTOM_SHEET_ORDER_REJECT"
        fun instance(
            restoId: String,
            selectedAction: (selectedDriver: String) -> Unit,
            type: String = "",
        ): BottomSheetChooseDriverFragment {
            BottomSheetChooseDriverFragment().apply {
                this.selectedAction = selectedAction
                this.restoId = restoId
                this.type = type
                return this
            }
        }
    }

    private lateinit var binding: MainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
    }

    private fun initUI() {
        binding.tvTitleFilter.setOnClickListener {
            Toast.makeText(
                requireContext(),
                mAdapter.itemCount.toString() + mAdapter.data.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        mAdapter.notifyDataSetChanged()

        viewModel.getDriverOnResto(this.restoId)
        mAdapter.setupAdapterInterface(object : ManageDriverAdapter.ItemInterface {
            override fun onclick(model: UserModel) {
                if (model.isDriverAvailable())
                    showChooseDriverConfirmationDialog(model)
                else {
                    binding.root.showSnackbar(
                        getString(R.string.title_driver_unavailable),
                        SnackbarType.ERROR
                    )
                }
            }
        })
    }

    private fun showChooseDriverConfirmationDialog(model: UserModel) {
        // Suppose id = 1111 and name = neil (just what you want). 
        val s = SpannableStringBuilder(getString(R.string.message_change_driver))
            .bold { append(model.name) }
            .append("\n")
            .append(getString(R.string.message_order_status_will_change_to))
            .append("\n")
            .bold { getString(R.string.status_on_delivery) }

        DialogUtils.showConfirmationDialog(
            context = requireContext(),
            title = getString(R.string.label_are_you_sure),
            messageSpannable = s,
            positiveAction = Pair("OK") {
                selectedAction.invoke(model.id.toString())
                dismiss()
            },
            negativeAction = Pair(
                "No",
                { }),
            autoDismiss = true,
            buttonAllCaps = false
        )
    }

    fun initObserver() {
        viewModel.driverOnRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let {
                        mAdapter.setWithNewData(it.getDrivers())
                        mAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            requireContext(),
                            "${mAdapter.itemCount}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
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


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


}
