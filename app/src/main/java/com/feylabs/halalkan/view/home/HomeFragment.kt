package com.feylabs.halalkan.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.PostResponse
import com.feylabs.halalkan.data.remote.reqres.UserResponse
import com.feylabs.halalkan.databinding.FragmentHomeBinding
import com.feylabs.halalkan.utils.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    // save temporary user list
    private var listUsers = mutableListOf<UserResponse.UserResponseItem>()

    private val mAdapter by lazy { ListPostsAdapter() }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun initAction() {}

    override fun initData() {
        viewModel.fetchUsers()
    }

    override fun initUI() {
        initRecyclerView()
        initAdapter()
    }

    private fun initAdapter() {
        mAdapter.setupAdapterInterface(object : ListPostsAdapter.PostItemInterface {
            override fun onclick(model: ShowedPosts?) {
                findNavController().navigate(
                    R.id.postDetailFragment,
                    bundleOf(
                        "postModel" to model,
                    )
                )
            }

            override fun onUserClick(model: ShowedPosts?) {

            }

        })
    }

    private fun initRecyclerView() {
        binding.rvPosts.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = mAdapter
        }
    }

    override fun initObserver() {
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Default -> {
                    showLoading(false)
                }
                is Error -> {
                    showLoading(false)
                    showToast(it.message.toString())
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    listUsers.clear()
                    it.data.let { list ->
                        list.let {
                            it?.forEachIndexed { index, userResponseItem ->
                                listUsers.add(userResponseItem)
                            }
                        }
                    }

                    viewModel.fetchPosts()
                }
            }
        })


    }

    private fun showLoading(b: Boolean) {
        if (b) {
            viewGone(binding.rvPosts)
            viewVisible(binding.loading)
        } else {
            viewVisible(binding.rvPosts)
            viewGone(binding.loading)
        }
    }

    private fun combinePostAndUsers(data: PostResponse?) {
        val tempShowedPosts = mutableListOf<ShowedPosts>()

        data?.forEachIndexed { index, postResponseItem ->
            with(postResponseItem) {

                val userCred: Pair<String, String> = findUserCred(userId)

                tempShowedPosts.add(
                    ShowedPosts(
                        id = id.toString(),
                        title = title.toString(),
                        body = body.toString(),
                        userName = userCred.first,
                        userCompanyName = userCred.second,
                        userEmail = findUserEmail(userId),
                        userId = userId.toString()
                    )
                )
            }
        }

        mAdapter.data.clear()
        mAdapter.setWithNewData(tempShowedPosts.toMutableList())
        mAdapter.notifyDataSetChanged()
    }

    private fun findUserCred(userId: Int): Pair<String, String> {
        var userName = ""
        var userCompany = ""
        listUsers.forEachIndexed { index, userResponseItem ->
            if (userId.toString() == userResponseItem.id.toString()) {
                userName = userResponseItem.name
                userCompany = userResponseItem.company.name
            }
        }

        return Pair(userName, userCompany)
    }

    private fun findUserEmail(userId: Int): String {
        var userEmail = ""
        listUsers.forEachIndexed { index, userResponseItem ->
            if (userId.toString() == userResponseItem.id.toString()) {
                userEmail = userResponseItem.email
            }
        }

        return userEmail
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}