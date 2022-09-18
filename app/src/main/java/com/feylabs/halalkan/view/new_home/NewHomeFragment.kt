package com.feylabs.halalkan.view.new_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.AskPermissionDialog
import com.feylabs.halalkan.data.local.MyPreference
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidModelResponse
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidResponseWithoutPagination
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.data.remote.reqres.resto.AllRestoNoPagination
import com.feylabs.halalkan.data.remote.reqres.resto.RestoModelResponse
import com.feylabs.halalkan.databinding.FragmentNewHomeBinding
import com.feylabs.halalkan.utils.PermissionCommandUtil
import com.feylabs.halalkan.utils.PermissionUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isBlocked
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isNotGranted
import com.feylabs.halalkan.utils.TimeUtil.getCurrentTimeUnix
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.location.LocationUtils
import com.feylabs.halalkan.utils.location.MyLatLong
import com.feylabs.halalkan.utils.masjid.MasjidUtility.renderWithDistanceModel
import com.feylabs.halalkan.utils.resto.OrderUtility
import com.feylabs.halalkan.utils.resto.RestoUtility.renderWithDistanceModel
import com.feylabs.halalkan.utils.snackbar.UtilSnackbar
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import com.feylabs.halalkan.view.resto.main.RestoMainAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class NewHomeFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
    val restoViewModel: RestoViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()


    private val restoAdapter by lazy { RestoMainAdapter() }
    private val mMosqueAdapter by lazy { ListMasjidAdapter() }

    private var _binding: FragmentNewHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {
        hideStatusBar()

        binding.greetingImg.setOnClickListener {
            val list = listOf(
                R.drawable.bg_header_daylight,
                R.drawable.bg_header_dawn,
                R.drawable.bg_header_evening,
                R.drawable.bg_header_night,
                R.drawable.bg_header_sunrise
            )
            binding.greetingImg.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    list.random()
                )
            )
        }
        setupMenuClickListener()
    }

    private fun hideStatusBar() {
        val decorView = requireActivity().window.decorView // Hide the status bar.

        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = uiOptions
    }

    override fun onResume() {
        super.onResume()
        if (mMosqueAdapter.itemCount == 0) {
            fetchAllMasjid()
        }
    }

    private fun setupMenuClickListener() {

        binding.bottomNav.apply {

            setBottomMenuActive(tvHome)
            setBottomMenuActive(ivMenuHome)

            btnMenuProfile.setOnClickListener {
                val userToken = muskoPref().getToken().orEmpty()
                if (userToken.isEmpty()) {
                    UtilSnackbar.showSnackbar(getRootView(), "Silakan Login Terlebih Dahulu")
                    findNavController().navigate(R.id.navigation_loginFragment)
                } else {
                    findNavController().navigate(R.id.navigation_userProfileFragment)
                }
            }
        }

        binding.menuPrayer.setOnClickListener {
            findNavController().navigate(R.id.navigation_prayerMainFragment)
        }
        binding.menuResto.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_newHomeFragment_to_navigation_restoMainFragment)
        }
        binding.menuTranslate.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_newHomeFragment_to_navigation_translateFragment)
        }

        binding.menuForum.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_newHomeFragment_to_forumHomeFragment)
        }

        binding.menuScan.setOnClickListener {
            findNavController().navigate(R.id.navigation_scanProductFragment)
        }

        binding.menuProduct.setOnClickListener {
            findNavController().navigate(R.id.navigation_productCategoryListFragment)
        }
    }

    override fun initData() {
        val user = MyPreference(requireContext()).getUserData()
        if (user.rolesId != 3 && user.rolesId != 1) {
            fetchPrayerTime()
            fetchAllMasjid()
            fetchAllResto()
        }
    }

    private fun fetchAllResto() {
        restoViewModel.getAllRestoRaw()
    }

    private fun fetchAllMasjid() {
        if (mMosqueAdapter.itemCount == 0)
            viewModel.fetchAllMasjid()
    }

    private fun fetchPrayerTime() {
        viewModel.fetchPrayerTimeSingle(
            latitude = mainViewModel.liveLatLng.value?.lat ?: 0.0,
            longitude = mainViewModel.liveLatLng.value?.long ?: 0.0,
            method = "11",
            time = getCurrentTimeUnix()
        )

    }

    override fun initUI() {
        imageGreet()
        checkRole()
        setupPermission()
        initRecyclerView()
        initAdapter()
    }

    private fun checkRole() {
        val user = MyPreference(requireContext()).getUserData()
        if (user.rolesId == 3) {
            findNavController().navigate(R.id.action_navigation_newHomeFragment_to_navigation_initAdminRestoFragment)
        } else if (user.rolesId == 4) {
            findNavController().navigate(R.id.action_navigation_newHomeFragment_to_navigation_driverOrderFragment)
        }
    }

    private fun initAdapter() {
        restoAdapter.setupAdapterInterface(object : RestoMainAdapter.ItemInterface {
            override fun onclick(model: RestoModelResponse) {
                if (OrderUtility(requireContext()).checkResto(model.id).not()) {
                    OrderUtility(requireContext()).clearOrder()
                }
                findNavController().navigate(
                    R.id.navigation_detailRestoFragment, bundleOf(
                        "data" to model
                    )
                )
            }
        })

        mMosqueAdapter.setupAdapterInterface(object : ListMasjidAdapter.ItemInterface {
            override fun onclick(model: MasjidModelResponse) {
                findNavController().navigate(
                    R.id.navigation_detailMasjidFragment,
                    bundleOf("data" to model)
                )
            }
        })
    }

    private fun initRecyclerView() {
        binding.rvResto.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = restoAdapter
        }

        binding.rvMasjid.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = mMosqueAdapter
        }

    }

    override fun initObserver() {

        restoViewModel.allRestoRawLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    showLoadingResto(false)
                }
                is QumparanResource.Error -> {
                    showLoadingResto(false)
                }
                is QumparanResource.Loading -> {
                    showLoadingResto(true)
                }
                is QumparanResource.Success -> {
                    it.data?.let { response ->
                        setupRestoData(response)
                    }
                }
            }
        }

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

        viewModel.prayerTimeSingleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {
                    binding.shimmerViewJadwal.showShimmer(true)
                }
                is QumparanResource.Error -> {
                    binding.shimmerViewJadwal.hideShimmer()
                }
                is QumparanResource.Loading -> {
                    binding.shimmerViewJadwal.showShimmer(true)
                }
                is QumparanResource.Success -> {
                    binding.shimmerViewJadwal.hideShimmer()
                    it.data?.let { response ->
                        setupPrayerTimeLiveData(response)
                    }
                }
            }
        }

        mainViewModel.liveAddress.observe(viewLifecycleOwner) {}

        mainViewModel.liveKecamatan.observe(viewLifecycleOwner) {
            binding.layoutLandingMessage.apply {
                tvLocation.text = it
            }
        }

        mainViewModel.liveLatLng.observe(viewLifecycleOwner) {
            fetchPrayerTime()
            fetchAllMasjid()
        }

    }

    private fun setupRestoData(response: AllRestoNoPagination) {
        var initialData = response.data.toMutableList()

        if (LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)) {
            initialData = initialData.renderWithDistanceModel(
                myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0, -99.0),
                sortByNearest = true, limit = 10
            )
        }

        restoAdapter.setWithNewData(initialData)
        restoAdapter.notifyDataSetChanged()
        showLoadingResto(false)
    }

    private fun setupPrayerTimeLiveData(response: PrayerTimeAladhanSingleDateResponse) {
        val data = response.data
        data.timings.let { timings ->
            binding.includeDatePray.apply {
                txtTimeAshar.text = timings.asr
                txtTimeDhuhur.text = timings.dhuhr
                txtTimeIsya.text = timings.isha
                txtTimeSubuh.text = timings.fajr
                txtTimeMagrib.text = timings.maghrib
            }
        }
    }

    private fun setupMosqueData(response: MasjidResponseWithoutPagination) {

        var initialData = response.data.toMutableList()

        if (LocationUtils.checkIfLocationSet(mainViewModel.liveLatLng.value)) {
            initialData = initialData.renderWithDistanceModel(
                myLocation = mainViewModel.liveLatLng.value ?: MyLatLong(-99.0, -99.0),
                sortByNearest = true, limit = 10
            )
        }

        mMosqueAdapter.setWithNewData(initialData)
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
            viewVisible(binding.loadingResto.root)
        } else {
            viewVisible(binding.rvResto)
            viewGone(binding.loadingResto.root)
        }
    }

    private fun imageGreet() {
        val greeting_img = binding.greetingImg
        val calendar = Calendar.getInstance()
        val timeOfDay = calendar[Calendar.HOUR_OF_DAY]
        when (timeOfDay) {
            in 0..5 -> {
                greeting_img.setImageResource(R.drawable.bg_header_dawn)
            }
            in 6..11 -> {
                greeting_img.setImageResource(R.drawable.bg_header_evening)
            }
            in 12..15 -> {
                greeting_img.setImageResource(R.drawable.bg_header_daylight)
            }
            in 16..17 -> {
                greeting_img.setImageResource(R.drawable.bg_header_evening)
            }
            in 18..23 -> {
                greeting_img.setImageResource(R.drawable.bg_header_night)
            }
        }
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


    private fun setupPermission() {
        setupPermissionLocation()
    }

    private fun setupPermissionLocation(): Boolean {
        val dialogBuilder = AskPermissionDialog.Builder(requireContext())
        val permLocCoarse = PermissionUtil.getPermissionStatus(
            requireActivity(),
            PermissionUtil.PER_LOCATION_COARSE
        )
        val permLocFine =
            PermissionUtil.getPermissionStatus(requireActivity(), PermissionUtil.PER_LOCATION_FINE)

        if (permLocCoarse.isNotGranted() || permLocFine.isNotGranted()) {
            showToast("NEVER GRANTED")
            if (permLocCoarse.isBlocked() || permLocFine.isBlocked()) {
                dialogBuilder.text("Aplikasi membutuhkan akses lokasi anda untuk menghitung jarak dan jadwal sholat")
                    .setImageBuilder(R.drawable.menu_home_prayer)
                    .positiveAction {
                        PermissionUtil.openSetting(requireContext())
                    }
                    .build().show(binding.root)
            } else {
                dialogBuilder.text("Aplikasi membutuhkan akses lokasi anda untuk menghitung jarak dan jadwal sholat")
                    .setImageBuilder(R.drawable.menu_home_prayer)
                    .positiveAction {
                        PermissionCommandUtil.homeLocation(requireActivity())
                    }.build().show(binding.root)
            }
            return false
        } else {
            // do nothing
            return true
        }
    }

}