package com.feylabs.halalkan.view.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.data.remote.reqres.MenteeResponse
import com.feylabs.halalkan.databinding.ExamplePaginationFragmentBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.home.HomeViewModel
import com.kemendag.ppid.ui.example.ExamplePaginationAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class ExamplePaginationFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: ExamplePaginationFragmentBinding? = null
    private val binding get() = _binding!!


    private val mAdapter by lazy { ExamplePaginationAdapter() }

    override fun initUI() {
        hideActionBar()
        binding.rvInformation.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        mAdapter.setupAdapterInterface(object : ExamplePaginationAdapter.ItemInterface {
            override fun onclick(model: MenteeResponse.Data) {

            }

            override fun loadMore(page: Int) {
                var currentPage = mAdapter.page
                //viewModel.fetchMentee(++currentPage)
            }
        })
    }

    override fun initObserver() {
        /*
        viewModel.menteeLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is QumparanResource.Default -> {
                }
                is QumparanResource.Error -> {
                }
                is QumparanResource.Loading -> {
                }
                is QumparanResource.Success -> {
                    it.data?.let { metaData ->
                        if (metaData.data.size < 0) {
                            showToast("Tidak Ada Data")
                        } else {
                            // if this is not the first page
                            if (metaData.currentPage != 1) {
                                setupNewPageMentee(metaData)
                            }

                            if (metaData.currentPage == 1) {
                                mAdapter.page = metaData.currentPage
                                mAdapter.addNewData(metaData.data.toMutableList())
                            }
                        }
                    }
                    binding.tvPage.text = mAdapter.page.toString()
                    binding.rvInformation.smoothScrollToPosition(
                        mAdapter.itemCount-1
                    )
                }
            }
        })
         */
    }

    private fun setupNewPageMentee(metaData: MenteeResponse) {
        mAdapter.page = metaData.currentPage
        mAdapter.addNewData(metaData.data.toMutableList())
    }

    private fun showLoadingArticle(b: Boolean) {
    }

    override fun initAction() {
        binding.btnLoad.setOnClickListener {
            addNewData()
        }

        binding.btnCount.setOnClickListener {
            showToast(mAdapter.itemCount.toString())
        }
    }

    private fun addNewData() {
        val list = mutableListOf<MenteeResponse.Data>()
        mAdapter.addNewData(list)
    }

    private fun fetchNewMentee(page: Int) {
        //viewModel.fetchMentee(page)
    }

    override fun initData() {
        //viewModel.fetchMentee(1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  _binding = ExamplePaginationFragmentBinding.inflate(inflater)
        return binding.root
    }


}