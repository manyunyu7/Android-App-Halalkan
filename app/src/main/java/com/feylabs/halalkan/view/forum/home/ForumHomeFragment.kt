package com.feylabs.halalkan.view.forum.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetGeneralAction
import com.feylabs.halalkan.customview.bottomsheet.MyBottomSheetAction
import com.feylabs.halalkan.customview.bottomsheet.MyBottomSheetAction.*
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.forum.AllForumPaginationResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse
import com.feylabs.halalkan.databinding.FragmentForumHomeBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.forum.ForumViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class ForumHomeFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentForumHomeBinding? = null
    private val binding get() = _binding!!

    var totalReviewPage = 0
    private val viewModel: ForumViewModel by viewModel()

    private val mAdapter by lazy { ForumAdapter() }
    var currentPosition = 0

    override fun onResume() {
        super.onResume()
        mAdapter.clearData()
        mAdapter.notifyDataSetChanged()
    }

    override fun initUI() {
        binding.btnLoadMore.makeGone()
        setupAdapterRv()
        setupEmptyLayout()
    }

    private fun setupEmptyLayout() {
        binding.apply {
            tvEmptyDesc.text = "No Forum Yet"
            btnEmptyAction.makeGone()
        }
    }

    private fun setupAdapterRv() {
        binding.rvForum.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        binding.rvForum.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    binding.btnLoadMore.makeVisible()
//                } else {
//                    binding.btnLoadMore.makeGone()
//                }
            }
        })

        mAdapter.setupAdapterInterface(object : ForumAdapter.ItemInterface {
            override fun onLike(model: ForumModelResponse) {
                viewModel.likeForum(model.id)
            }

            override fun onUnLike(model: ForumModelResponse) {
                viewModel.unlikeForum(model.id)
            }

            override fun onShare(model: ForumModelResponse) {
            }

            override fun onclick(model: ForumModelResponse) {
                findNavController().navigate(
                    R.id.navigation_forumDetailFragment, bundleOf(
                        "id" to model.id.toString()
                    )
                )
            }

            override fun onAction(model: ForumModelResponse, adapterPosition:Int) {
                currentPosition = adapterPosition
                showBottomSheetAction(model)
            }

            override fun loadMore(page: Int) {
                loadNextPage()
            }
        })
    }

    private fun showBottomSheetAction(model: ForumModelResponse) {
        val olz: (selectedId: String, action: MyBottomSheetAction) -> Unit = { selectedId, action ->
            when (action) {
                EDIT -> {
                    findNavController().navigate(
                        R.id.navigation_forumCreateThreadFragment,
                        bundleOf("forumId" to model.id.toString())
                    )
                }
                SEE -> {
                    findNavController().navigate(
                        R.id.navigation_forumDetailFragment,
                        bundleOf("id" to model.id.toString())
                    )
                }
                CREATE -> {}
                DELETE -> {
                    deleteForum(model.id)
                }
                OTHER -> {}
            }
        }
        BottomSheetGeneralAction.instance(
            description = "Choose Action",
            title = "Choose Action",
            selectedAction = olz,
            objectId = model.id.toString(),
            ownerId = model.userId.toString()
        ).show(getMFragmentManager(), BottomSheetGeneralAction().tag)
    }

    private fun deleteForum(id: Int) {
        DialogUtils.showConfirmationDialog(
            context = requireContext(),
            title = "Are You Sure",
            message = "This action will delete this post",
            positiveAction = Pair("OK") {
                mAdapter.data.removeAt(currentPosition)
                mAdapter.notifyItemRemoved(currentPosition)
               viewModel.deleteForum(id)
            },
            negativeAction = Pair(
                "No",
                { showToast("Canceled") }),
            autoDismiss = true,
            buttonAllCaps = false
        )

    }

    private fun loadNextPage() {
        val currentPage = mAdapter.page
        if (currentPage >= totalReviewPage)
            showSnackbar("You are on the last page")
        else
            viewModel.getForumPagination(page = currentPage + 1)
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
                        viewModel.fireAllForumLiveData()
                    }
                }
                is QumparanResource.Default -> {}
            }
        }
    }

    private fun setupReviewFromNetwork(response: AllForumPaginationResponse) {
        val reviewRes = response
        reviewRes.let {
            if (it.data.isEmpty()) {
                if (it.currentPage == 1) {
                    showEmptyLayout(true)
                }
            } else {
                showEmptyLayout(false)
                totalReviewPage = it.lastPage

                if (it.currentPage == 1) {
                    // if this is a first page
                    mAdapter.page = it.currentPage
                    mAdapter.setWithNewData(it.data.toMutableList())

                } else {
                    mAdapter.page = it.currentPage
                    mAdapter.addNewData(it.data.toMutableList())
                    binding.rvForum.scrollToPosition(mAdapter.data.size)
                }

                // check if this is last page....
                // if so hide the load more button
                if (it.currentPage == it.total) {
                    binding.btnLoadMore.makeGone()
                } else {
                    binding.btnLoadMore.makeVisible()
                }
            }
        }
    }


    private fun showLoading(b: Boolean) {
        if (b) {
            binding.includeLoading.root.makeVisible()
        } else {
            binding.includeLoading.root.makeGone()
        }
    }

    override fun initAction() {
        binding.btnCreate.setOnClickListener {
            if (muskoPref().getToken().isEmpty()) {
                showSnackbar("Please Login First")
            } else
                findNavController().navigate(R.id.navigation_forumCreateThreadFragment)
        }

        binding.btnLoadMore.setOnClickListener {
            loadNextPage()
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