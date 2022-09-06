package com.feylabs.halalkan.view.resto.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetGeneralAction
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.data.remote.reqres.resto.SearchRestoResponse
import com.feylabs.halalkan.databinding.FragmentAllRestoBinding
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.resto.RestoUtility.renderWithDistanceModel
import com.feylabs.halalkan.view.favorite.FavViewModel
import com.feylabs.halalkan.view.resto.BottomSheetFilterResto
import com.feylabs.halalkan.view.resto.RestoViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class AllRestoFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentAllRestoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RestoViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()
    private val favViewModel: FavViewModel by viewModel()

    var totalReviewPage = 0

    var scrolled = 0


    companion object {
        const val SCROLLED_POSITION = "DASDC"
    }

    private val mAdapter by lazy { AllRestoAdapter() }

    override fun onResume() {
        super.onResume()
        val index = MyPreference(requireContext()).getPrefInt(SCROLLED_POSITION) ?: 0
        binding.rvList.smoothScrollBy(0, index)
    }

    override fun initUI() {
        arguments?.getString("title")?.let {
            binding.labelPageTitleTopbar.text = it
        }
        setupAdapterRv()

        if (isFromLike()) {
            binding.containerXyz.makeGone()
        }
    }

    private fun isFromNearest(): Boolean {
        val obj = arguments?.getString("nearest").orMuskoEmpty("")
        return obj.isNotEmpty()
    }

    private fun isFromLike(): Boolean {
        val obj = arguments?.getString("like").orMuskoEmpty("")
        return obj.isNotEmpty()
    }

    private fun setupAdapterRv() {
        binding.rvList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isSaveEnabled = true

            if (isFromLike()) {
                mAdapter.isFromLike = true
            }
        }

        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                scrolled += dy
                MyPreference(requireContext()).save(SCROLLED_POSITION, scrolled)
            }
        })


        mAdapter.setupAdapterInterface(object : AllRestoAdapter.ItemInterface {
            override fun onLike(model: RestoModelResponse) {
                favViewModel.addFavResto(model.id.toString())
            }

            override fun onUnLike(model: RestoModelResponse) {
                favViewModel.removeFavResto(model.id.toString())
            }

            override fun onclick(model: RestoModelResponse) {
                findNavController().navigate(
                    R.id.navigation_detailRestoFragment,
                    bundleOf("data" to model)
                )
            }

            override fun loadMore(page: Int) {
                val currentPage = mAdapter.page
                fetchData(page = currentPage + 1)
            }
        })
    }

    private fun isFromTypeFood(): Boolean {
        val fromIntent = arguments?.getString("type_food_id")?.toIntOrNull()
        return fromIntent != null
    }

    private fun isFromCert(): Boolean {
        val fromIntent = arguments?.getString("certification_id")?.toIntOrNull()
        return fromIntent != null
    }

    private fun getSearchTypeFoodId(): Int? {
        if (isFromTypeFood()) {
            val fromIntent = arguments?.getString("type_food_id")?.toIntOrNull()
            return fromIntent
        } else {
            return MyPreference(requireContext()).getNullableInt("filterRestoTypeFood")
        }
    }

    private fun getSearchName(): String? {
        val text = binding.searchView.query.toString().orEmpty()
        if (text == "") return null
        else
            return binding.searchView.query.toString()
    }

    private fun getSearchCertificationId(): Int? {
        if (isFromCert()) {
            val fromIntent = arguments?.getString("certification_id")?.toIntOrNull()
            return fromIntent
        } else {
            return MyPreference(requireContext()).getNullableInt("filterCertTypeFood")
        }
    }


    override fun initObserver() {
        viewModel.searchRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoading(false)
                }
                is QumparanResource.Error -> {
                    showLoading(false)
                }
                is QumparanResource.Loading -> {
                    showLoading(true)
                }
                is QumparanResource.Success -> {
                    showLoading(false)
                    it.data?.let { response ->
                        setupDataFromNetwork(response)

                        if (isFromNearest()) {
                            setupDataForNearest(response.data)
                        } else {

                        }
                    }
                }
            }
        }
    }

    private fun setupDataForNearest(data: List<RestoModelResponse>) {
        var initialData = data.toMutableList()

        if (LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)) {
            initialData = initialData.renderWithDistanceModel(
                myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0, -99.0),
                sortByNearest = true, limit = 999
            )
        }

        mAdapter.apply {
            setWithNewData(initialData)
            notifyDataSetChanged()
        }
    }

    private fun setupDataFromNetwork(response: SearchRestoResponse) {
        val reviewRes = response
        reviewRes.let {
            if (it.data.isEmpty()) {
                if (it.currentPage == 1) {
                    mAdapter.clearData()
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
                    binding.rvList.scrollToPosition(mAdapter.data.size)
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

    private fun showEmptyLayout(b: Boolean) {
        if (b) {
            binding.layoutEmptyState.makeVisible()
        } else {
            binding.layoutEmptyState.makeGone()
        }
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            viewVisible(binding.loadingAnim)
        } else {
            viewGone(binding.loadingAnim)
        }
    }


    override fun initAction() {

        binding.btnLoadMore.setOnClickListener {
            loadNextPage()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                fetchData()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.btnFilter.setOnClickListener {
            showBottomSheetFilter()
        }


        binding.rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mAdapter.loadMore()
                }
            }
        })
    }

    private fun loadNextPage() {
        val currentPage = mAdapter.page
        if (currentPage >= totalReviewPage)
            showSnackbar("You are on the last page")
        else
            fetchData(currentPage + 1)
    }

    private fun showBottomSheetFilter() {
        val olz: (notes: String) -> Unit = { notes ->
            fetchData()
        }
        var type = ""
        if (isFromCert()) {
            type = "cert"
        }

        if (isFromTypeFood()) {
            type = "typeFood"
        }

        showToast(type)
        BottomSheetFilterResto.instance(
            selectedAction = olz,
            type = type
        ).show(getMFragmentManager(), BottomSheetGeneralAction().tag)
    }

    override fun initData() {
        if (mAdapter.itemCount == 0) {
            fetchData()
        }
    }

    private fun fetchData(page: Int = 1) {
        showToast("name : ${getSearchName()} certification : ${getSearchCertificationId()} type: ${getSearchTypeFoodId()}")

        var sortBy: String? = null

        if (isFromNearest()) {
            sortBy = "distance"
        }

        viewModel.searchResto(
            page = page,
            certificationId = getSearchCertificationId(),
            name = getSearchName(),
            typeFoodId = getSearchTypeFoodId(),
            sortBy = sortBy
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllRestoBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}