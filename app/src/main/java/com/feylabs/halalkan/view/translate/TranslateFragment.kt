package com.feylabs.halalkan.view.translate

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.feylabs.halalkan.customview.SearchLangDialogFragment
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.databinding.CustomViewSearchLanguageDialogBinding
import com.feylabs.halalkan.databinding.FragmentTranslateBinding
import com.feylabs.halalkan.utils.TranslatorUtil
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.view.ContainerActivity
import com.feylabs.halalkan.view.utilview.searchwithimage.ListSearchWithImageAdapter
import com.feylabs.halalkan.view.utilview.searchwithimage.SearchWithImageModel
import com.murgupluoglu.flagkit.FlagKit
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*


class TranslateFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentTranslateBinding? = null
    private val binding get() = _binding!!

    val viewmodel: TranslateViewModel by viewModel()

    lateinit var searchLangDialog: SearchLangDialogFragment
    lateinit var searchLangDialogBinding: CustomViewSearchLanguageDialogBinding


    override fun initUI() {
        searchLangDialog = SearchLangDialogFragment()

        initLanguageContainerUI()
    }

    private fun initLanguageContainerUI() {
        viewmodel.sourceLanguage.value = "id"
        viewmodel.targetLanguage.value = "ko"
        viewmodel.sourceLanguageDesc.value = "Indonesia"
        viewmodel.targetLanguageDesc.value = "Korea"

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
                    setupLanguageTTS()
                    val sourceContainer = binding.containerSourceLanguage
                    val targetCountainer = binding.containerTargetLanguage

                    binding.layoutDropdownTranslator.apply {
                        if (viewmodel.clickedContainer.value == 1) {
                            viewmodel.sourceLanguage.value = model.code
                            viewmodel.sourceLanguageDesc.value = model.name
                            labelLanguage1.text = model.name
                            sourceContainer.labelLanguage.text = model.name
                            //showToast("source is ${model.name}")
                        }

                        if (viewmodel.clickedContainer.value == 2) {
                            viewmodel.targetLanguage.value = model.code
                            viewmodel.targetLanguageDesc.value = model.name
                            labelLanguage2.text = model.name
                            targetCountainer.labelLanguage.text = model.name
                            //showToast("target is ${model.name}")
                        }

                        // fetch new translation
                        viewmodel.getTranslation(sourceContainer.etTranslate.text.toString())

                        searchLangDialog.dismiss()
                    }
                }
            })
        }
    }

    override fun initObserver() {
        viewmodel.translateLiveData.observe(viewLifecycleOwner) {
            showToast(it.data.toString() + it.message.toString())
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {}
                is QumparanResource.Loading -> {}
                is QumparanResource.Success -> {
                    it.data?.let { translateResponse ->
                        if (translateResponse.status != 0) {
                            //if translation is success
                            val simpleTranslate = translateResponse.data.simple_translate.toString()
                            val simpleSpelling = translateResponse.data.spelling
                            binding.containerTargetLanguage.etTranslate.setText(simpleTranslate)
                            binding.containerTargetLanguage.etSpelling.setText(simpleSpelling)
                        }
                    }
                }
            }
        }

        viewmodel.targetLanguage.observe(viewLifecycleOwner) {
            TranslatorUtil.loadFlagImage(
                requireContext(),
                binding.layoutDropdownTranslator.imgFlag2,
                it.toString()
            )
        }

        viewmodel.sourceLanguage.observe(viewLifecycleOwner) {
            TranslatorUtil.loadFlagImage(
                requireContext(),
                binding.layoutDropdownTranslator.imgFlag1,
                it.toString()
            )
        }

        // observe lang desc
        viewmodel.targetLanguageDesc.observe(viewLifecycleOwner) {
            binding.containerTargetLanguage.labelLanguage.text = it.toString()
            binding.layoutDropdownTranslator.labelLanguage2.text = it.toString()
        }
        viewmodel.sourceLanguageDesc.observe(viewLifecycleOwner) {
            binding.containerSourceLanguage.labelLanguage.text = it.toString()
            binding.layoutDropdownTranslator.labelLanguage1.text = it.toString()
        }

        viewmodel.ttlLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is QumparanResource.Default -> {}
                is QumparanResource.Error -> {}
                is QumparanResource.Loading -> {}
                is QumparanResource.Success -> {
                    it.data?.let { response ->
                        playMediaSource(response)
                    }
                }
            }
        }
    }

    private fun playMediaSource(response: TiktokTextToSpeechResponse) {
        if (response.message == "success") {
            val audioBase64 = response.data.vStr
            val duration = response.data.duration
            playBase64Media(audioBase64)
        }
    }

    override fun initAction() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        handleLanguageSwitch()
        handleTranslate()
        setupLanguageTTS()

        binding.layoutDropdownTranslator.containerLang1.setOnClickListener {
            viewmodel.clickedContainer.value = 1
            searchLangDialog.show(getMFragmentManager(), "langPicker")
        }

        binding.layoutDropdownTranslator.containerLang2.setOnClickListener {
            viewmodel.clickedContainer.value = 2
            searchLangDialog.show(getMFragmentManager(), "langPicker2")
        }

    }

    private fun handleTranslate() {
        var timer = Timer()
        val DELAY: Long = 3000 // Milliseconds
        binding.containerSourceLanguage.etTranslate.doAfterTextChanged {
            binding.containerTargetLanguage.etTranslate.setText(".....")
            if (it.toString().isNotEmpty()) {
                val text = it.toString()
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            // You will probably need to use
                            // runOnUiThread(Runnable action) for some
                            // specific actions (e.g., manipulating views).
                            viewmodel.getTranslation(text)
                        }
                    },
                    DELAY
                )
            }
        }

    }

    private fun handleLanguageSwitch() {
        binding.layoutDropdownTranslator.logoSwitch.setOnClickListener {
            val currentTextOnTarget = binding.containerTargetLanguage.etTranslate.text
            binding.containerSourceLanguage.etTranslate.text = currentTextOnTarget
            val currentTextOnSource = binding.containerSourceLanguage.etTranslate.text
            viewmodel.apply {
                val currentSource = sourceLanguage.value.toString()
                val currentTarget = targetLanguage.value.toString()
                val currentSourceDesc = sourceLanguageDesc.value.toString()
                val currentTargetDesc = targetLanguageDesc.value.toString()

                binding.layoutDropdownTranslator.apply {
                    sourceLanguage.value = currentTarget
                    sourceLanguageDesc.value = currentTargetDesc
                    labelLanguage1.text = currentTargetDesc
                    binding.containerSourceLanguage.labelLanguage.text = currentTargetDesc

                    targetLanguage.value = currentSource
                    targetLanguageDesc.value = currentSourceDesc
                    labelLanguage2.text = currentSourceDesc
                    binding.containerTargetLanguage.labelLanguage.text = currentSourceDesc
                }
            }
            if (currentTextOnSource.toString().isNotEmpty())
                viewmodel.getTranslation(currentTextOnSource.toString())
        }
    }

    private fun setupLanguageTTS() {
        binding.containerSourceLanguage.apply {
            imgSpeaker.setOnClickListener {
                val text = this.etTranslate.text.toString()
                if (text.isNotEmpty()) {
                    viewmodel.getTextToSpeech(text, isSource = true)
                }
            }
        }

        binding.containerTargetLanguage.apply {
            imgSpeaker.setOnClickListener {
                val text = this.etTranslate.text.toString()
                if (text.isNotEmpty()) {
                    viewmodel.getTextToSpeech(text, isSource = false)
                }
            }
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

    fun playBase64Media(base64: String) {
        try {
            val url = "data:audio/mp3;base64,$base64"
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()
                mediaPlayer.setVolume(100f, 100f)
                mediaPlayer.setLooping(false)
            } catch (e: IllegalArgumentException) {
                showToast("You might not set the DataSource correctly!")
            } catch (e: SecurityException) {
                showToast("You might not set the DataSource correctly!")
            } catch (e: IllegalStateException) {
                showToast("You might not set the DataSource correctly!")
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mediaPlayer.setOnPreparedListener { player -> player.start() }
            mediaPlayer.setOnCompletionListener { mp ->
                mp.stop()
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}