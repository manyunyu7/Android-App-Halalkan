package com.feylabs.halalkan.view.new_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.databinding.FragmentNewHomeBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.home.ListPostsAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class NewHomeFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentNewHomeBinding? = null

    private val mAdapter by lazy { ListRestaurantAdapter() }
    private val mMosqueAdapter by lazy { ListMasjidAdapter() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {
        binding.menuPrayer.setOnClickListener {
            findNavController().navigate(R.id.navigation_prayerMainFragment)
        }

        binding.menuResto.setOnClickListener {

        }

        binding.menuTranslate.setOnClickListener {
            findNavController().navigate(R.id.navigation_translateFragment)
        }

        binding.menuScan.setOnClickListener {

        }
    }

    override fun initData() {
        viewModel.fetchAllMasjid()
    }

    override fun initUI() {
        initRecyclerView()
        initAdapter()
        loadData()
    }

    private fun initAdapter() {
        mAdapter.setupAdapterInterface(object : ListRestaurantAdapter.ItemInterface {
            override fun onclick(model: RestaurantHomeUIModel) {

            }
        })

        mMosqueAdapter.setupAdapterInterface(object : ListMasjidAdapter.ItemInterface {
            override fun onclick(model: MasjidResponseWithoutPagination.Data) {

            }

        })
    }

    private fun initRecyclerView() {
        binding.rvResto.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = mAdapter
        }

        binding.rvMasjid.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = mMosqueAdapter
        }

    }

    override fun initObserver() {
        viewModel.allMasjidLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoadingMosque(false)
                }
                is QumparanResource.Error -> {
                    showLoadingMosque(false)
                }
                is QumparanResource.Loading -> {
                    showLoadingMosque(true)
                }
                is QumparanResource.Success -> {
                    it.data?.let { response ->
                        setupMosqueData(response)
                    }
                }
            }
        }
    }

    private fun setupMosqueData(response: MasjidResponseWithoutPagination) {
        mMosqueAdapter.setWithNewData(response.data.toMutableList())
        mMosqueAdapter.notifyDataSetChanged()
        showLoadingMosque(false)
    }

    private fun showLoadingMosque(b: Boolean) {
        if (b) {
            viewGone(binding.rvMasjid)
            viewVisible(binding.loadingMosque.root)
        } else {
            viewVisible(binding.rvMasjid)
            viewGone(binding.loadingMosque.root)
        }
    }


    private fun showLoadingResto(b: Boolean) {
        if (b) {
            viewGone(binding.rvResto)
//            viewVisible(binding.loading)
        } else {
            viewVisible(binding.rvResto)
//            viewGone(binding.loading)
        }
    }

    private fun loadData() {
        val tempShowedPosts = mutableListOf<RestaurantHomeUIModel>()

        tempShowedPosts.add(
            RestaurantHomeUIModel(
                title = "La Yeon at The Shilla Seoul",
                categoryTop = "SELF CERTIFIED",
                categoryMiddle = "General Restaurant",
                address = "The Shilla Seoul, 249 Dongho-ro, Jangchung-dong, Jung-gu, Seoul, South Korea",
                image = "https://a.cdn-hotels.com/gdcs/production188/d997/13be6186-c914-42a9-8ee1-a868ea66b49e.jpg?impolicy=fcrop&w=1600&h=1066&q=medium",
                id = "1",
                cuisineCategory = "Korean Cuisine",
                distance = "15Km",
                star = "4.5"
            ),
        )
        tempShowedPosts.add(
            RestaurantHomeUIModel(
                title = "Pierre Gagnaire a Seoul",
                categoryTop = "SELF CERTIFIED",
                categoryMiddle = "General Restaurant",
                address = "35F, Lotte Hotel Seoul, 1 Sogong-dong, Jung-gu, Seoul, South Korea",
                image = "https://a.cdn-hotels.com/gdcs/production132/d1493/c941da47-f4fb-4b9a-b69a-8f84d34e5cb7.jpg?impolicy=fcrop&w=1600&h=1066&q=medium",
                id = "1",
                cuisineCategory = "French Cuisine",
                distance = "5Km",
                star = "3.5"
            ),
        )
        tempShowedPosts.add(
            RestaurantHomeUIModel(
                title = "Jungsik",
                categoryTop = "SELF CERTIFIED",
                categoryMiddle = "General Restaurant",
                address = "Seolleungro 158-gil, Gangnam-gu, Seoul, South Korea",
                image = "https://a.cdn-hotels.com/gdcs/production189/d1007/83712783-0d4b-406e-ad4f-4dff326269ff.jpg?impolicy=fcrop&w=1600&h=1066&q=medium",
                id = "1",
                cuisineCategory = "Korean Cuisine",
                distance = "11Km",
                star = "3"
            ),
        )
        tempShowedPosts.add(
            RestaurantHomeUIModel(
                title = "Tosokchon",
                categoryTop = "HALAL Certified",
                categoryMiddle = "General Restaurant",
                address = "5, Jahamun-ro 5-gil, Jongno-gu, Seoul, South Korea",
                image = "https://a.cdn-hotels.com/gdcs/production88/d994/b2916ff8-a231-484f-8c3b-c0e0540778d4.jpg?impolicy=fcrop&w=1600&h=1066&q=medium",
                id = "",
                cuisineCategory = "Korean Cuisine",
                distance = "33Km",
                star = "5"
            ),
        )

        mAdapter.data.clear()
        mAdapter.setWithNewData(tempShowedPosts.toMutableList())
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentNewHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}