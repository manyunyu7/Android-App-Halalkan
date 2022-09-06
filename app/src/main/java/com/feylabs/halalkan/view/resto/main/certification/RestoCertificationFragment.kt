package com.feylabs.halalkan.view.resto.main.certification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.databinding.FragmentTypeFoodBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.resto.RestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class RestoCertificationFragment : BaseFragment() {

    private var _binding: FragmentTypeFoodBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { RestoCertificationAdapter() }

    val viewModel: RestoViewModel by viewModel()

    override fun initUI() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter.setupAdapterInterface(object : RestoCertificationAdapter.ItemInterface {
            override fun onclick(model: RestaurantCertificationResponse.RestaurantCertificationItem) {
                findNavController().navigate(
                    R.id.navigation_allRestoFragment,
                    bundleOf(
                        "certification_id" to model.id.toString(),
                        "title" to model.name,
                    )
                )
            }
        })

        binding.rv.let {
            it.layoutManager = setLayoutManagerLinear()
            it.adapter = mAdapter
        }

    }

    override fun initObserver() {
        viewModel.certLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {

                }
                is QumparanResource.Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString())
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it?.data?.let {
                        setupUiFromNetwork(it)
                    }
                }
            }
        }
    }

    private fun setupUiFromNetwork(it: RestaurantCertificationResponse) {
        mAdapter.setWithNewData(it)
        mAdapter.notifyDataSetChanged()
    }

    private fun showLoading(b: Boolean) {
        if (b)
            binding.loadingAnim.makeVisible()
        else binding.loadingAnim.makeGone()
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    override fun initData() {
        if (mAdapter.itemCount == 0)
            viewModel.getRestoCert()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTypeFoodBinding.inflate(inflater)
        return binding.root
    }

}