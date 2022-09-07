package com.feylabs.halalkan.view.prayer.prayer_time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.prayertime.PrayerTimeAladhanSingleDateResponse
import com.feylabs.halalkan.databinding.FragmentPrayerTimeBinding
import com.feylabs.halalkan.utils.TimeUtil
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.new_home.ListMasjidAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PrayerTimeFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
    val mainViewModel: MainViewModel by sharedViewModel()
    private var _binding: FragmentPrayerTimeBinding? = null


    private val mMosqueAdapter by lazy { ListMasjidAdapter() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {
    }

    override fun initData() {
        binding.tvDate.text = printLocalDate()
        fetchPrayerTime()

        binding.btnNextDate.setOnClickListener {
            val dateFormat = SimpleDateFormat("dd/M/yyyy")
            val cal = Calendar.getInstance()
            cal.time = dateFormat.parse(binding.tvDate.text.toString())
            cal.add(Calendar.DATE, +1)

            val strdate = dateFormat.format(cal.time);
            binding.tvDate.text = strdate

            fetchPrayerTime(date = getUnixDateTime(strdate).toString())
        }

        binding.btnBeforeDate.setOnClickListener {
            val dateFormat = SimpleDateFormat("dd/M/yyyy")
            val cal = Calendar.getInstance()
            cal.time = dateFormat.parse(binding.tvDate.text.toString())
            cal.add(Calendar.DATE, -1)

            val strdate = dateFormat.format(cal.time);
            binding.tvDate.text = strdate

            fetchPrayerTime(date = getUnixDateTime(strdate).toString())

        }
    }


    private fun fetchPrayerTime(date:String = TimeUtil.getCurrentTimeUnix()) {
        viewModel.fetchPrayerTimeSingle(
            latitude = mainViewModel.liveLatLng.value?.lat ?: 0.0,
            longitude = mainViewModel.liveLatLng.value?.long ?: 0.0,
            method = "11",
            time = date
        )
    }


    private fun getUnixDateTime(s: String): String? {
        val date = SimpleDateFormat("dd/M/yyyy").parse(s)
        return date.time.toString()
    }

    private fun printLocalDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        return currentDate
    }

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.prayerTimeSingleLiveData.observe(viewLifecycleOwner) {
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
                        setupPrayerTimeLiveData(response)
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

    private fun setupPrayerTimeLiveData(response: PrayerTimeAladhanSingleDateResponse) {
        val data = response.data
        data.timings.let { timings ->
            binding.tvAsrTime.text = timings.asr
            binding.tvZuhrTime.text = timings.dhuhr
            binding.tvIsyaTime.text = timings.isha
            binding.tvFajrTime.text = timings.fajr
            binding.tvMaghribTime.text = timings.maghrib
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPrayerTimeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}