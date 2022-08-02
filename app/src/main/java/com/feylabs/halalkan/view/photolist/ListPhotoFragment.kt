package com.feylabs.halalkan.view.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.AlbumPhotoResponse
import com.feylabs.halalkan.databinding.FragmentListPhotoBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.postdetail.PostDetailViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListPhotoFragment : BaseFragment() {


    private var _binding: FragmentListPhotoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mAdapter by lazy { PhotoListAdapter() }

    val viewModel: PostDetailViewModel by viewModel()

    override fun initUI() {
        setupAdapter()
        setupRv()
        hideActionBar()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRv() {
        binding.rvPosts.let {
            it.adapter = mAdapter
            it.layoutManager = GridLayoutManager(requireContext(), 2)
            it.setHasFixedSize(true)
        }
    }

    private fun setupAdapter() {
        mAdapter.setupAdapterInterface(object : PhotoListAdapter.PostItemInterface {
            override fun onclick(model: AlbumPhotoResponse.AlbumPhotoResponseItem?) {
                findNavController().navigate(
                    R.id.previewPhotoFragment,
                    bundleOf("url" to model?.url.toString(), "title" to model?.title.toString())
                )
            }
        })
    }

    override fun initObserver() {
        viewModel.photosLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is QumparanResource.Default -> {
                }
                is QumparanResource.Error -> {
                    showToast(it.message.toString())
                }
                is QumparanResource.Loading -> {
                }
                is QumparanResource.Success -> {
                    it.data?.let {
                        setupPhoto(it)
                    }
                }
            }
        })
    }

    private fun setupPhoto(it: AlbumPhotoResponse) {
        mAdapter.setWithNewData(it.toMutableList())
        mAdapter.notifyDataSetChanged()
    }

    override fun initAction() {

    }

    override fun initData() {
        val albumId = arguments?.getString("albumId")
        viewModel.getPhotosByAlbum(albumId.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


}