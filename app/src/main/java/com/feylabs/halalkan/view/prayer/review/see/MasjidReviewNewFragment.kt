package com.feylabs.halalkan.view.prayer.review.see

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.databinding.FragmentReviewNewMasjidBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MasjidReviewNewFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentReviewNewMasjidBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PrayerRoomViewModel by viewModel()

    private val mAdapter by lazy { MasjidReviewAdapter() }

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

        mAdapter.setupAdapterInterface(object : MasjidReviewAdapter.ItemInterface {
            override fun onclick(model: MasjidReviewPaginationResponse.Reviews.Data) {

            }

            override fun loadMore(page: Int) {
                val currentPage = mAdapter.page
                viewModel.getMasjidReview(getMasjidId(), page = currentPage + 1)
            }
        })
    }

    private fun getMasjidId(): String {
        return arguments?.getString("id") ?: ""
    }

    override fun initObserver() {
        viewModel.masjidReviewsLiveData.observe(viewLifecycleOwner) {
            if (it is QumparanResource.Loading) showLoading(true) else showLoading(false)
            when (it) {
                is QumparanResource.Error -> {
                    showToast("Error")
                }
                is QumparanResource.Loading -> {
                }
                is QumparanResource.Success -> {
                    it.data?.let { response ->
                        setupReviewFromNetwork(response)
                    }
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

    private fun setupReviewFromNetwork(response: MasjidReviewPaginationResponse) {
        val reviewRes = response.reviews
        reviewRes.let {
            if (it.data ==null) {
                if (it.currentPage == 1) {
                    showEmptyLayout(true)
                }
            } else {
                showEmptyLayout(false)
                if (it.currentPage == 1) {
                    mAdapter.page = it.currentPage
                    mAdapter.setWithNewData(it.data.toMutableList())
                    showToast("Menambahkan data halaman pertama /${reviewRes.currentPage}")
                } else {
                    if (it.lastPage == it.currentPage) {
                        showSnackbar("Anda Berada di Halaman Terakhir", SnackbarType.INFO)
                    } else {
                        mAdapter.page = it.currentPage
                        mAdapter.addNewData(it.data.toMutableList())
                    }
                }
            }
        }

        binding.startStats.setStarCount(response.reviewCount.avg)
        binding.startStats.setStartBarUi(response.reviewCount)
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
            R.id.navigation_masjidCreateReviewFragment, bundleOf(
                "id" to getMasjidId()
            )
        )
    }

    override fun initAction() {

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
        viewModel.getMasjidReview(getMasjidId(), page = 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewNewMasjidBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}