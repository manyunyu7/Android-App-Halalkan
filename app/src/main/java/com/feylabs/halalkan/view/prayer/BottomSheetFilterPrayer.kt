package com.feylabs.halalkan.view.prayer

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidTypeResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidTypeResponse.MasjidTypeResponseItem
import com.feylabs.halalkan.databinding.LayoutBottomsheetFilterPrayerBinding as MainBinding
import com.feylabs.halalkan.utils.CommonUtil.makeGone
import com.feylabs.halalkan.utils.CommonUtil.makeVisible
import com.feylabs.halalkan.utils.ViewUtil.dpToPixels
import com.feylabs.halalkan.view.resto.admin_resto.driver.ManageDriverAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import org.koin.android.viewmodel.ext.android.viewModel

class BottomSheetFilterPrayer : BottomSheetDialogFragment() {

    private lateinit var selectedAction: (text: String) -> Unit

    private var type = ""

    val viewModel by viewModel<PrayerRoomViewModel>()
    private val mAdapter by lazy { ManageDriverAdapter() }


    companion object {
        private val TAG = "BOTTOM_SHEET_RESTO_FILTER"
        fun instance(
            selectedAction: (text: String) -> Unit,
            type: String = "",
        ): BottomSheetFilterPrayer {
            BottomSheetFilterPrayer().apply {
                this.selectedAction = selectedAction
                this.type = type
                return this
            }
        }
    }

    private fun setupChipMasjidType(data: MasjidTypeResponse) {
        val mdata = data.toMutableList()
        Toast.makeText(requireContext(), data.toString(), Toast.LENGTH_SHORT).show()
        mdata.add(
            0,
            MasjidTypeResponseItem(-99, getString(R.string.title_all))
        )
        mdata.forEachIndexed { index, statusOrder ->
            val chip = Chip(requireContext())
            chip.text = statusOrder.name
            chip.tag = statusOrder.id
            chip.isCheckable = true

            if (statusOrder.id == getFilterRestoTypeFood()) {
                chip.isChecked = true
            }

            chip.setOnClickListener {
                if (chip.tag==-99){
                    saveFilterRestCert(null)
                }else{
                    saveFilterRestCert(chip.tag.toString().toIntOrNull())
                }
            }
//            chip.setOnCheckedChangeListener { compoundButton, b ->
//                if (chip.tag==-99){
//                    saveFilterRestCert(null)
//                }else{
//                    saveFilterRestCert(chip.tag.toString().toIntOrNull())
//                }
//            }

            chip.setTextColor(Color.parseColor("#000000"))
            chip.chipStrokeColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_stroke_color
                    )
                )
            chip.chipStrokeWidth = 1.dpToPixels(requireContext())
            chip.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_text_color
                    )
                )
            )
            chip.chipBackgroundColor =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.chip_filter_selection_color
                    )
                )
            binding.chipGroupType.addView(chip)
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
        initData()
        initObserver()
    }

    private fun initData() {
        viewModel.getMasjidType()
    }

    private fun initUI() {
        binding.btnApplyFilter.setOnClickListener {
            dismiss()
            selectedAction.invoke("")
        }
    }


    fun initObserver() {
        viewModel.masjidTypeLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Success -> {
                    showLoading(false)
                    it?.data?.let {
                        setupChipMasjidType(it)
                    }
                }
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {

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

    private fun getFilterRestoTypeFood(): Int? {
        return MyPreference(requireContext()).getNullableInt("filterRestoTypeFood")
    }

    private fun saveFilterRestoTypeFood(value: Int?) {
        value?.let {
            if (value == -99) {
                MyPreference(requireContext()).removeKey("filterRestoTypeFood")
            } else {
                MyPreference(requireContext()).save("filterRestoTypeFood", value)
            }
        } ?: run {
            MyPreference(requireContext()).removeKey("filterRestoTypeFood")
        }
    }


    private fun getFilterRestoCert(): Int? {
        return MyPreference(requireContext()).getNullableInt("filterCertTypeFood")
    }

    private fun saveFilterRestCert(value: Int?) {
        value?.let {
            if (value == -99) {
                MyPreference(requireContext()).removeKey("filterCertTypeFood")
            } else {
                MyPreference(requireContext()).save("filterCertTypeFood", value)
            }
        } ?: run {
            MyPreference(requireContext()).removeKey("filterCertTypeFood")
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        selectedAction.invoke("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }


}
