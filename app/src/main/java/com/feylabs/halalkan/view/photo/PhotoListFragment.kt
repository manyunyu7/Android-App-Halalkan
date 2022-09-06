package com.feylabs.halalkan.view.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.customview.imagepreviewcontainer.CustomViewPhotoModel
import com.feylabs.halalkan.data.remote.reqres.masjid.MasjidPhotosResponse
import com.feylabs.halalkan.databinding.*
import com.feylabs.halalkan.utils.StringUtil.orMuskoEmpty
import com.feylabs.halalkan.utils.base.BaseFragment


class PhotoListFragment : BaseFragment() {

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!
    private val mAdapter by lazy { PhotoListAdapter() }

    override fun initUI() {
        arguments?.getString("pageTitle")?.let {
            binding.pageTitle.text = it
        }

        initAdapter()

        mAdapter.setWithNewData(getPhotoData())
        mAdapter.notifyDataSetChanged()
    }

    private fun initAdapter() {
        binding.rv.layoutManager = setLayoutManagerGridVertical(3)
        binding.rv.adapter = mAdapter


        mAdapter.setupAdapterInterface(object : PhotoListAdapter.ItemInterface {
            override fun onclick(model: CustomViewPhotoModel) {
            }
        })
    }


    override fun initObserver() {

    }

    private fun getPhotoData(): MutableList<CustomViewPhotoModel> {
        val tempList = mutableListOf<CustomViewPhotoModel>()

        val masjids = arguments?.getParcelable<MasjidPhotosResponse>("photos")
        masjids?.let {
            it.forEachIndexed { index, s ->
                tempList.add(CustomViewPhotoModel(name = "", url = s))
            }
        }

        val restos = arguments?.getStringArrayList("photos")
        restos?.forEachIndexed { index, s ->
            tempList.add(CustomViewPhotoModel(name = "", url = s))
        }

        return tempList
    }

    private fun showEmptyLayout(b: Boolean) {
        if (b) {
            binding.stateNoData.makeVisible()
        } else {
            binding.stateNoData.makeGone()
        }
    }


    private fun showLoading(b: Boolean) {
        if (b)
            binding.loadingAnim.makeVisible()
        else binding.loadingAnim.makeGone()
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {

    }

    private fun getProductId(): String {
        return arguments?.getString("productId").orMuskoEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoListBinding.inflate(inflater)
        return binding.root
    }
}