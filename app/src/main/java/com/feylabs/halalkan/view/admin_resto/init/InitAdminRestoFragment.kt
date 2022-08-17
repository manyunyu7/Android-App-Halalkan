package com.feylabs.halalkan.view.admin_resto.init

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.RazkyGalleryActivity
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.databinding.FragmentXrestoInitBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.admin_resto.AdminRestoViewModel
import com.tangxiaolv.telegramgallery.GalleryConfig
import org.koin.android.viewmodel.ext.android.viewModel


class InitAdminRestoFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoInitBinding? = null
    private val binding get() = _binding!!
    private val PERMISSION_CODE_STORAGE = 1001

    val viewModel by viewModel<AdminRestoViewModel>()

    val nearbyRestoAdapter by lazy { RestoAdminMainAdapter() }

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
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.navigation_addEditRestoFragment)
        }

        binding.labelRestoAroundYou.setOnClickListener {
            muskoPref().clearPreferences()
            findNavController().popBackStack(R.id.navigation_newHomeFragment,true)
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