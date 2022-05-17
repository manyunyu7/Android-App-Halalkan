package com.feylabs.halalkan.customview

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.CustomViewSearchLanguageDialogBinding
import com.feylabs.halalkan.view.utilview.searchwithimage.ListSearchWithImageAdapter

class SearchLangDialogFragment : DialogFragment() {


    private var _binding: CustomViewSearchLanguageDialogBinding? = null

    // This property is only valid between onCreateDialog and
    // onDestroyView.
    val binding get() = _binding!!



    val adapter by lazy { ListSearchWithImageAdapter() }

    lateinit private var langDialogFragmentInterface: SearchLangDialogFragmentInterface

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = CustomViewSearchLanguageDialogBinding.inflate(LayoutInflater.from(context))
        langDialogFragmentInterface.getBinding(binding)

        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.setHasFixedSize(true)

        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }


    fun setDialogInterface(mInterface: SearchLangDialogFragmentInterface) {
        this.langDialogFragmentInterface = mInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_view_search_language_dialog, container)
    }


    interface SearchLangDialogFragmentInterface {
        fun getBinding(binding: CustomViewSearchLanguageDialogBinding)
    }

}

