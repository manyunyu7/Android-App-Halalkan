package com.feylabs.halalkan.view.resto.admin_resto.init

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.databinding.FragmentXrestoInitBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class InitAdminRestoFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoInitBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    val viewModel by viewModel<AdminRestoViewModel>()

    val nearbyRestoAdapter by lazy { RestoAdminMainAdapter() }

    override fun onPause() {
        revokeViewModel()
        super.onPause()
    }

    override fun onStop() {
        revokeViewModel()
        super.onStop()
    }

    private fun revokeViewModel() {
        viewModel.createRestoLiveData.removeObserver { }
        viewModel.myRestoLiveData.removeObserver { }
    }

    override fun initUI() {
        binding.rvRestoNearby.apply {
            layoutManager = setLayoutManagerGridVertical(2)
            adapter = nearbyRestoAdapter
        }

        nearbyRestoAdapter.setupAdapterInterface(object : RestoAdminMainAdapter.ItemInterface {
            override fun onclick(model: RestoModelResponse) {
                MyPreference(requireContext()).save("CHOSEN_RESTO", model.id.toString())
                findNavController().navigate(
                    R.id.navigation_currentRestaurantFragment,
                    bundleOf(
                        "data" to model,
                        "id" to model.id.toString(),
                        "name" to model.name.toString(),
                    )
                )
            }
        })
    }

    override fun initObserver() {
        viewModel.myRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is QumparanResource.Loading -> {}
                is QumparanResource.Success -> {
                    setupRestoData(it.data)
                }
            }
        }
    }

    override fun initAction() {
        binding.btnLogout.setOnClickListener {
            DialogUtils.showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.message_logged_out),
                positiveAction = Pair("OK") {
                    revokeViewModel()
                    muskoPref().clearPreferences()
                    findNavController().popBackStack(R.id.navigation_newHomeFragment,true)
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigation_addEditRestoFragment)
        }

        binding.labelRestoAroundYou.setOnClickListener {
            muskoPref().clearPreferences()
            findNavController().popBackStack(R.id.navigation_newHomeFragment, true)
        }
    }

    override fun initData() {
        viewModel.getMyResto()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentXrestoInitBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRestoData(data: AllRestoNoPagination?) {
        data?.let {
            nearbyRestoAdapter.apply {
                setWithNewData(data.data.toMutableList())
                notifyDataSetChanged()
            }
        }
    }


}