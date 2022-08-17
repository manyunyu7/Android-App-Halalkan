package com.feylabs.halalkan.view.forum.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.forum.AllForumPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidReviewPaginationResponse
import com.feylabs.halalkan.databinding.FragmentForumHomeBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.forum.ForumViewModel
import com.feylabs.halalkan.view.prayer.review.see.MasjidReviewAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class ForumHomeFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentForumHomeBinding? = null
    private val binding get() = _binding!!

    var totalReviewPage = 0
    private val viewModel: ForumViewModel by viewModel()

    private val mAdapter by lazy { ForumAdapter() }


    override fun initUI() {
        setupAdapterRv()
    }

    private fun setupAdapterRv() {
        binding.rvForum.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        mAdapter.setupAdapterInterface(object : ForumAdapter.ItemInterface {

            override fun onclick(model: ForumModelResponse) {

            }

            override fun loadMore(page: Int) {
                val currentPage = mAdapter.page
                if (currentPage >= totalReviewPage)
                    showSnackbar("Anda Sudah Berada di Halaman Terakhir")
                else
                    viewModel.getForumPagination(page = currentPage + 1)
            }
        })
    }


    override fun initObserver() {
        viewModel.allForumLiveData.observe(viewLifecycleOwner) {
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

    private fun setupReviewFromNetwork(response: AllForumPaginationResponse) {
        val reviewRes = response
        reviewRes.let {
            if (it.data == null) {
                if (it.currentPage == 1) {
                    showEmptyLayout(true)
                }
            } else {
                showEmptyLayout(false)
                totalReviewPage = it.lastPage
                if (it.currentPage == 1) {
                    mAdapter.page = it.currentPage
                    mAdapter.setWithNewData(it.data.toMutableList())
                    showToast("Menambahkan data halaman pertama /${reviewRes.currentPage}")
                } else {
                    if (it.lastPage == mAdapter.page) {
                        showSnackbar("Anda Berada di Halaman Terakhir", SnackbarType.INFO)
                    } else {
                        mAdapter.page = it.currentPage
                        mAdapter.addNewData(it.data.toMutableList())
                    }
                }
            }
        }
    }


    private fun showLoading(b: Boolean) {

    }

    override fun initAction() {
        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.navigation_forumCreateThreadFragment)
        }
    }

    override fun initData() {
        viewModel.getForumPagination(1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showEmptyLayout(b: Boolean) {
        if (b) {
            binding.layoutEmptyState.makeVisible()
        } else {
            binding.layoutEmptyState.makeGone()
        }
    }

}