package com.feylabs.halalkan.view.forum.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.bottomsheet.BottomSheetGeneralAction
import com.feylabs.halalkan.customview.bottomsheet.MyBottomSheetAction
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.forum.CreateCommentPayload
import com.feylabs.halalkan.data.remote.reqres.forum.ForumCommentResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumDetailResponse
import com.feylabs.halalkan.data.remote.reqres.forum.ForumModelResponse
import com.feylabs.halalkan.databinding.FragmentForumDetailBinding
import com.feylabs.halalkan.utils.CommonUtil
import com.feylabs.halalkan.utils.DialogUtils
import com.feylabs.halalkan.utils.ImageViewUtils.loadImageFromURL
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.utils.snackbar.SnackbarType
import com.feylabs.halalkan.view.forum.ForumViewModel
import com.like.LikeButton
import com.like.OnLikeListener
import org.koin.android.viewmodel.ext.android.viewModel


class ForumDetailFragment : BaseFragment() {

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentForumDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForumViewModel by viewModel()

    private val mAdapter by lazy { ForumCommentAdapter() }

    var commentPosition = 0


    override fun initUI() {
        setupAdapterRv()
        binding.includeForum.root.makeGone()
        binding.includeForum.containerShare.makeGone()
    }

    private fun setupAdapterRv() {
        binding.rvForum.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        mAdapter.setupAdapterInterface(object : ForumCommentAdapter.ItemInterface {

            override fun onclick(
                model: ForumCommentResponse.ForumCommentResponseItem,
                position: Int
            ) {
                showBottomSheetComment(model)
                commentPosition = position
            }

            override fun onLiked(model: ForumCommentResponse.ForumCommentResponseItem) {
                viewModel.likeComment(model.id)
            }

            override fun onUnliked(model: ForumCommentResponse.ForumCommentResponseItem) {
                viewModel.unlikeComment(model.id)
            }
        })
    }


    override fun initObserver() {

        viewModel.createCommentLiveData.observe(viewLifecycleOwner){
            when(it){
                is Default -> {
                    showLoading(false)
                }
                is Error -> {
                    showLoading(false)
                }
                is Loading -> {
                    showLoading(true)
                }
                is Success -> {
                    showLoading(false)
                    binding.etComment.editText?.setText("")
                    viewModel.getCommentOnForum(getForumId())
                    hideSoftKeyboard()
                    showSnackbar("Your Comment Was Posted Successfully")
                }
            }
        }
        viewModel.commentOnForumLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Default -> {
                    showLoading(false)
                }
                is Error -> {
                    showSnackbar(it.message.toString(),SnackbarType.ERROR)
                    showLoading(false)
                }
                is Loading -> {
                    showLoading(false)
                }
                is Success -> {
                    it.data?.let {
                        setupCommentFromNetwork(it)
                    }
                }
            }
        }

        viewModel.deleteForumLiveData.observe(viewLifecycleOwner){
            if (it is Loading) showLoading(true) else showLoading(false)
            when(it){
                is Default -> {}
                is Error -> {}
                is Loading -> {
                }
                is Success ->  {
                    findNavController().popBackStack()
                    showSnackbar("Forum Deleted Successfully")
                }
            }
        }
        viewModel.deleteCommentLiveData.observe(viewLifecycleOwner){
            if (it is Loading) showLoading(true) else showLoading(false)
            when(it){
                is Default -> {}
                is Error -> {}
                is Loading -> {}
                is Success ->  {
                    mAdapter.notifyItemRemoved(commentPosition)
                    mAdapter.data.removeAt(commentPosition)
                    showSnackbar("Comment Deleted Successfully")
                }
            }
        }

        viewModel.detailForumLiveData.observe(viewLifecycleOwner) {
            if (it is Loading) showLoading(true) else showLoading(false)
            when (it) {
                is Error -> {
                    showLoading(false)
                    binding.includeForum.root.makeGone()
                    showSnackbar(it.message.toString(), SnackbarType.ERROR)
                }
                is Loading -> {
                    showLoading(true)
                    binding.includeForum.root.makeGone()
                }
                is Success -> {
                    showLoading(false)
                    binding.includeForum.root.makeVisible()
                    it.data?.let { response ->
                        setupDataFromNetwork(response)
                    }
                }
                is Default -> {
                    binding.includeForum.root.makeGone()
                }
            }
        }
    }

    private fun setupDataFromNetwork(forum: ForumDetailResponse) {
        val data = forum.forum


        binding.includeForum.btnActionForum.setOnClickListener {
            showBottomSheetAction(forum.forum)
        }

        binding.includeForum.apply {

            if (data.img != null) {
                photo.loadImageFromURL(requireContext(), data.img_full_path)
            } else {
                containerCover.makeGone()
            }

            var isLiked = false
            data.likes?.forEachIndexed { index, like ->
                if (muskoPref().getUserID().toString() == like.userId.toString()) {
                    isLiked = true
                }
            }

            btnLike.isLiked = isLiked

            tvLikeCount.text = data.getLikeCount().toString()
            tvCommentCount.text = data.getCommentCount().toString()
            tvTitle.text = data.title
            tvQuestion.text = data.body

            includeUserProfile.apply {
                labelUserName.text=data.user?.name
                labelCategory.text=data.category?.name
                ivMainImage.loadImageFromURL(requireContext(),data.user?.imgFullPath)
            }
        }
    }

    private fun showBottomSheetComment(model: ForumCommentResponse.ForumCommentResponseItem) {
        val olz: (selectedId: String, action: MyBottomSheetAction) -> Unit = { selectedId, action ->
            when (action) {
                MyBottomSheetAction.EDIT -> {}
                MyBottomSheetAction.SEE -> {}
                MyBottomSheetAction.CREATE -> {}
                MyBottomSheetAction.DELETE -> { deleteComment(id) }
                MyBottomSheetAction.OTHER -> {}
            }
        }
        BottomSheetGeneralAction.instance(
            description = "Choose Action",
            title = "Choose Action",
            selectedAction = olz,
            objectId = model.id.toString(),
            ownerId = model.userId.toString(),
            type = "comment"
        ).show(getMFragmentManager(), BottomSheetGeneralAction().tag)
    }

    private fun deleteComment(id: Int) {
        DialogUtils.showConfirmationDialog(
            context = requireContext(),
            title = "Are You Sure",
            message = "This action will delete this comment",
            positiveAction = Pair("OK") {
                viewModel.deleteComment(id)
            },
            negativeAction = Pair(
                "No",
                { showToast("Canceled") }),
            autoDismiss = true,
            buttonAllCaps = false
        )
    }

    private fun showBottomSheetAction(model: ForumModelResponse) {
        val olz: (selectedId: String, action: MyBottomSheetAction) -> Unit = { selectedId, action ->
            when (action) {
                MyBottomSheetAction.EDIT -> {
                    findNavController().navigate(
                        R.id.navigation_forumCreateThreadFragment,
                        bundleOf("forumId" to model.id.toString())
                    )
                }
                MyBottomSheetAction.SEE -> {
                    findNavController().navigate(
                        R.id.navigation_forumDetailFragment,
                        bundleOf("id" to model.id.toString())
                    )
                }
                MyBottomSheetAction.CREATE -> {}
                MyBottomSheetAction.DELETE -> {
                    deleteForum(model.id)
                }
                MyBottomSheetAction.OTHER -> {}
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
        with(DialogUtils) {
            showConfirmationDialog(
                context = requireContext(),
                title = getString(R.string.label_are_you_sure),
                message = getString(R.string.label_this_action_will_delete_post),
                positiveAction = Pair("OK") {
                    viewModel.deleteForum(id)
                },
                negativeAction = Pair(
                    getString(R.string.title_no),
                    { showToast(getString(R.string.label_canceled)) }),
                autoDismiss = true,
                buttonAllCaps = false
            )
        }
    }



    private fun setupCommentFromNetwork(comments: ForumCommentResponse?) {
        comments?.let {
            mAdapter.setWithNewData(it)
            mAdapter.notifyDataSetChanged()
        }
    }


    private fun showLoading(b: Boolean) {
        if (b){
            binding.includeLoading.root.makeVisible()
        }else{
            binding.includeLoading.root.makeGone()
        }
    }

    override fun initAction() {


        binding.etComment.editText?.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrEmpty().not()){
                binding.etComment.error=null
            }
        }

        if (CommonUtil.isLoggedIn(requireContext())) {
            binding.btnAddComment.setOnClickListener {
                var isError=false
                val comment = binding.etComment.editText?.text.toString()
                if(comment.isEmpty()){
                    isError=true
                    binding.etComment.error="Check Your Input"
                }

                if (isError.not()){
                    DialogUtils.showConfirmationDialog(
                        context = requireContext(),
                        title = "Are You Sure",
                        message = "Your Comment will be posted to the forum",
                        positiveAction = Pair("OK") {
                            viewModel.addComment(
                                CreateCommentPayload(
                                    comment = comment,
                                    getForumId()
                                )
                            )
                        },
                        negativeAction = Pair(
                            "No",
                            { }),
                        autoDismiss = false,
                        buttonAllCaps = false
                    )
                }
            }

            binding.includeForum.btnLike.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    val likeCount = binding.includeForum.tvLikeCount.toString().toIntOrNull() ?: 0
                    viewModel.likeForum(getForumId())
                    binding.includeForum.tvLikeCount.text = (likeCount + 1).toString()
                }

                override fun unLiked(likeButton: LikeButton) {
                    val currentLikeCount =
                        binding.includeForum.tvLikeCount.text.toString().toIntOrNull() ?: 0
                    if (currentLikeCount != 0) {
                        binding.includeForum.tvLikeCount.text =
                            (currentLikeCount - 1).toString()
                        viewModel.unlikeForum(getForumId())
                    }
                }
            })
        } else {
            binding.includeForum.btnLike.isEnabled = false
            binding.includeForum.btnLike.setOnClickListener {
                showSnackbar("Login First")
            }
        }
    }

    override fun initData() {
        viewModel.getCommentOnForum(getForumId())
        viewModel.getForumDetail(getForumId())
    }

    private fun getForumId(): Int {
        return arguments?.getString("id")?.toIntOrNull() ?: -99
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showEmptyLayout(b: Boolean) {
//        if (b) {
//            binding.layoutEmptyState.makeVisible()
//        } else {
//            binding.layoutEmptyState.makeGone()
//        }
    }

}