package com.feylabs.halalkan.view.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.customview.SearchLangDialogFragment
import com.feylabs.halalkan.databinding.CustomViewSearchLanguageDialogBinding
import com.feylabs.halalkan.databinding.FragmentListAndSearchPrayerRoomBinding
import com.feylabs.halalkan.databinding.FragmentTranslateBinding
import com.feylabs.halalkan.utils.TranslatorUtil
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.utilview.searchwithimage.ListSearchWithImageAdapter
import com.feylabs.halalkan.view.utilview.searchwithimage.SearchWithImageModel


class TranslateFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentTranslateBinding? = null
    private val binding get() = _binding!!

    lateinit var searchLangDialog: SearchLangDialogFragment
    lateinit var searchLangDialogBinding: CustomViewSearchLanguageDialogBinding

    private var sourceLanguage = ""
    private var targetLanguage = ""
    private var clickedContainer = 0 //1 or 2, 1 : source, 2 : target

    override fun initUI() {
        searchLangDialog = SearchLangDialogFragment()
        searchLangDialog.setDialogInterface(object :
            SearchLangDialogFragment.SearchLangDialogFragmentInterface {
            override fun getBinding(binding: CustomViewSearchLanguageDialogBinding) {
                searchLangDialogBinding = binding
            }
        })

        // setup translator dialog
        searchLangDialog.adapter.apply {
            setWithNewData(TranslatorUtil.getLanguageArray())
            setupAdapterInterface(object : ListSearchWithImageAdapter.ItemInterface {
                override fun onclick(model: SearchWithImageModel) {
                    val sourceContainer = binding.containerSourceLanguage
                    val targetCountainer = binding.containerTargetLanguage

                    binding.layoutDropdownTranslator.apply {

                        if (clickedContainer == 1) {
                            sourceLanguage = model.code
                            labelLanguage1.text = model.name
                            sourceContainer.labelLanguage.text=model.name
                            showToast("source is ${model.name}")
                        }

                        if (clickedContainer == 2) {
                            targetLanguage = model.code
                            labelLanguage2.text = model.name
                            targetCountainer.labelLanguage.text=model.name
                            showToast("target is ${model.name}")
                        }

                        searchLangDialog.dismiss()
                    }
                }
            })
        }
    }

    override fun initObserver() {
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.layoutDropdownTranslator.containerLang1.setOnClickListener {
            clickedContainer = 1
            searchLangDialog.show(getMFragmentManager(), "langPicker")
        }

        binding.layoutDropdownTranslator.containerLang2.setOnClickListener {
            clickedContainer = 2
            searchLangDialog.show(getMFragmentManager(), "langPicker2")
        }

    }

    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTranslateBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}