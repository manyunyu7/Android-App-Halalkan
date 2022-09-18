package com.feylabs.halalkan.view.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.product.ProductDetailResponse
import com.feylabs.halalkan.databinding.*
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment
import com.mapbox.maps.extension.style.expressions.dsl.generated.all
import com.mapbox.maps.extension.style.expressions.dsl.generated.sum
import org.koin.android.viewmodel.ext.android.viewModel


class ProductDetailFragment : BaseFragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    val viewModel: ProductViewModel by viewModel()

    override fun initUI() {
        arguments?.getString("pageTitle")?.let {
            binding.pageTitle.text = it
        }
    }


    override fun initObserver() {
        viewModel.productDetailLiveData.observe(viewLifecycleOwner) {
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

    private fun setupUiFromNetwork(response: ProductDetailResponse) {
        val product = response.data
        binding.apply {
            tvTitle.text = product.name
            binding.tvCertificationInformation.text = product.certificationName

            binding.img.loadImageFromURL(requireContext(),product.imgFullPath)

            product.productInformation?.let {
                var summary = ""
                it.summary.forEachIndexed { index, s ->
                    summary = s + "\n"
                }

                var allergics = ""
                it.allergy.forEachIndexed { index, s ->
                    allergics = s + "\n"
                }

                var ingredients = ""
                it.ingredient.forEachIndexed { index, s ->
                    ingredients = s + "\n"
                }

                binding.tvAllergicInfo.text = allergics
                binding.tvSummary.text = summary
                binding.tvIngredientInformation.text = ingredients
            }
        }
    }

    private fun showEmptyLayout(b: Boolean) {
        if (b) {
            binding.stateNoData.makeVisible()
        } else {
            binding.stateNoData.makeGone()
        }
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
        viewModel.getProductDetail(getProductId())
    }

    private fun getProductId(): String {
        return arguments?.getString("productId").orMuskoEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(inflater)
        return binding.root
    }
}