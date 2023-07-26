package com.feylabs.halalkan.view.resto.admin_resto.edit_info.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestoDetailResponse
import com.feylabs.halalkan.databinding.FragmentXrestoEditVideoBinding
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class EditRestoVideoFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentXrestoEditVideoBinding? = null
    private val binding get() = _binding!!

    val viewModel by viewModel<AdminRestoViewModel>()

    private var mapRestoType = mutableMapOf<String, String>()

    override fun initUI() {
    }

    override fun initObserver() {
        viewModel.updateCommonLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    viewModel.getDetailResto(getChoosenResto())
                    DialogUtils.showSuccessDialog(
                        context = requireContext(),
                        title = "Success",
                        message = "Data Updated Successfully",
                        positiveAction = Pair("OK") {
                        },
                        autoDismiss = true,
                        buttonAllCaps = false
                    )
                }
            }
        }
        viewModel.detailRestoLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {}
                is Error -> {
                    showLoading(false)
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    it.data?.let {
                        setupUiFromNetwork(it)
                    }
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

    private fun setupUiFromNetwork(data: RestoDetailResponse) {
        val videoLink = data.data.detailResto.youtubeVideoLink
        val webView = binding.webView
        if(videoLink?.isEmpty() == true) {
            binding.containerVideo.makeGone()
        }else{
            binding.containerVideo.makeVisible()
            val video = data.data.detailResto.youtubeVideoLink
            binding.etVideo.editText?.setText(video)
            val videoString =
                "<iframe width=\"100%\" height=\"100%\" src=\"$video\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            webView.loadData(videoString, "text/html", "utf-8")
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient = WebChromeClient()
        }
    }

    override fun initAction() {
        binding.btnSave.setOnClickListener {
            val videoLink = binding.etVideo.editText?.text.toString()
            if (videoLink.isEmpty()) {
                showSnackbar("Please Check Your Input")
            } else
                DialogUtils.showConfirmationDialog(
                    context = requireContext(),
                    title = "Are You Sure",
                    message = "This action will updating your video link",
                    positiveAction = Pair("OK") {
                        viewModel.updateRestoVideo(
                            getChoosenResto(),
                            videoLink = videoLink,
                        )
                    },
                    negativeAction = Pair(
                        "No",
                        { }),
                    autoDismiss = true,
                    buttonAllCaps = false
                )
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun initData() {
        viewModel.getFoodType()
        viewModel.getDetailResto(getChoosenResto())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentXrestoEditVideoBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}