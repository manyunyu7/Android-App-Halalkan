package com.feylabs.halalkan.view.resto.order.detail

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.auth.UserModel
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.admin_resto.driver.ManageDriverAdapter
import com.feylabs.halalkan.databinding.LayoutBottomsheetChooseDriverBinding as MainBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.scope.currentScope
import org.koin.android.viewmodel.ext.android.viewModel

class BottomSheetChooseDriverFragment : BottomSheetDialogFragment() {

    private lateinit var selectedAction: (notes: String) -> Unit

    private var driverId: String = ""
    private var existingNotes: String = ""
    private var type = ""

    val viewModel by viewModel<DriverViewModel>()
    private val mAdapter by lazy { ManageDriverAdapter() }


    companion object {
        private val TAG = "BOTTOM_SHEET_ORDER_REJECT"
        fun instance(
            objectId: String,
            selectedAction: (notes: String) -> Unit,
            type: String = "",
            existingNotes: String = "",
        ): BottomSheetChooseDriverFragment {
            BottomSheetChooseDriverFragment().apply {
                this.selectedAction = selectedAction
                this.driverId = objectId
                this.type = type
                this.existingNotes = existingNotes
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
    }

    private fun initUI() {
        binding.rv.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        mAdapter.setupAdapterInterface(object :ManageDriverAdapter.ItemInterface{
            override fun onclick(model: UserModel) {
                selectedAction.invoke(driverId)
                dismiss()
            }

        })
    }

    fun initObserver() {
        viewModel.driverOnRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let {
                        mAdapter.setWithNewData(it.getDrivers())
                        mAdapter.notifyDataSetChanged()
                    }
                }
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(b: Boolean) {
        if(b){
            binding.loadingAnim.makeVisible()
        }else{
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
