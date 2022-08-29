package com.feylabs.halalkan.view.resto.review.see

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.forum.AllForumPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoReviewPaginationResponse
import com.feylabs.halalkan.databinding.FragmentReviewGeneralBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.prayer.review.RestoReviewViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class RestoReviewFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentReviewGeneralBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestoReviewViewModel by viewModel()

    private val mAdapter by lazy { RestoReviewAdapter() }

    var totalReviewPage = 0

    override fun onResume() {
        super.onResume()
    }

    override fun initUI() {
        setupAdapterRv()
    }

    private fun setupAdapterRv() {
        binding.rv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        mAdapter.setupAdapterInterface(object : RestoReviewAdapter.ItemInterface {
            override fun onclick(model: RestoReviewPaginationResponse.Reviews.Data) {

            }

            override fun loadMore(page: Int) {
                val currentPage = mAdapter.page
                if (currentPage >= totalReviewPage)
                    showSnackbar("Anda Sudah Berada di Halaman Terakhir")
                else
                    viewModel.getReview(getRestoId(), page = currentPage + 1)
            }
        })
    }

    private fun getType(): String {
        return arguments?.getString("type") ?: ""
    }

    private fun getRestoId(): String {
        return arguments?.getString("id") ?: ""
    }

    override fun initObserver() {
        viewModel.restoReviewsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Error -> {
                    showLoading(false)
                    showToast("Error")
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let { response ->
                        setupReviewFromNetwork(response)
                    }
                }
                else -> {
                    showLoading(false)
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

    private fun setupReviewFromNetwork(response: RestoReviewPaginationResponse) {
        val reviewRes = response.reviews
        reviewRes.let {
            if (it.data.isEmpty()) {
                if (it.currentPage == 1) {
                    showEmptyLayout(true)
                }
                binding.btnLoadMore.makeGone()
            } else {
                totalReviewPage = it.lastPage
                binding.btnLoadMore.makeVisible()
                showEmptyLayout(false)

                if (it.currentPage == 1) {
                    // if this is a first page
                    mAdapter.page = it.currentPage
                    mAdapter.setWithNewData(it.data.toMutableList())

                } else {
                    mAdapter.page = it.currentPage
                    mAdapter.addNewData(it.data.toMutableList())
                    binding.rv.scrollToPosition(mAdapter.data.size)
                }

                // check if this is last page....
                // if so hide the load more button
                if (it.currentPage == it.total) {
                    binding.btnLoadMore.makeGone()
                } else {
                    binding.btnLoadMore.makeVisible()
                }
            }

            binding.startStats.setStarCount(response.reviewCount.avg)
            binding.startStats.setStartBarUi(response.reviewCount)
        }
    }


    private fun showEmptyLayout(b: Boolean) {
        if (b) {
            binding.layoutEmptyState.makeVisible()
        } else {
            binding.layoutEmptyState.makeGone()
        }
    }

    private fun goToWriteReview() {
        findNavController().navigate(
            R.id.navigation_restoCreateReviewFragment, bundleOf(
                "id" to getRestoId()
            )
        )
    }

    private fun loadNextPage() {
        val currentPage = mAdapter.page
        if (currentPage >= totalReviewPage)
            showSnackbar(getString(R.string.you_are_on_the_last_page))
        else
            viewModel.getReview(getRestoId(), currentPage + 1)
    }

    override fun initAction() {

        binding.btnLoadMore.setOnClickListener {
            loadNextPage()
        }

        binding.btnNewReview.setOnClickListener {
            goToWriteReview()
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnWriteReview.setOnClickListener {
            goToWriteReview()
        }
    }

    override fun initData() {
        viewModel.getReview(getRestoId(), page = 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewGeneralBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}