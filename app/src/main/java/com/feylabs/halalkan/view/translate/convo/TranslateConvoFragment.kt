package com.feylabs.halalkan.view.translate.convo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.androidnetworking.AndroidNetworking
import com.feylabs.halalkan.R
import com.feylabs.halalkan.customview.AskPermissionDialog
import com.feylabs.halalkan.customview.SearchLangDialogFragment
import com.feylabs.halalkan.data.remote.QumparanResource
import com.feylabs.halalkan.data.remote.reqres.translator.TiktokTextToSpeechResponse
import com.feylabs.halalkan.databinding.CustomViewSearchLanguageDialogBinding
import com.feylabs.halalkan.utils.PermissionUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.getPermissionStatus
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isBlocked
import com.feylabs.halalkan.utils.PermissionUtil.Companion.isNotGranted
import com.feylabs.halalkan.utils.TranslatorUtil
import com.feylabs.halalkan.utils.base.BaseFragment
import com.feylabs.halalkan.customview.searchwithimage.ListSearchWithImageAdapter
import com.feylabs.halalkan.customview.searchwithimage.SearchWithImageModel
import com.feylabs.halalkan.data.remote.QumparanResource.*
import com.feylabs.halalkan.data.remote.reqres.resto.RestaurantCertificationResponse
import com.feylabs.halalkan.databinding.FragmentTranslateConvoBinding
import com.feylabs.halalkan.databinding.ItemChatBinding
import com.feylabs.halalkan.utils.PermissionCommandUtil
import com.feylabs.halalkan.utils.PermissionUtil.Companion.openSetting
import com.feylabs.halalkan.utils.TimeUtil
import com.feylabs.halalkan.view.translate.TranslateViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*


class TranslateConvoFragment : BaseFragment() {


    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _binding: FragmentTranslateConvoBinding? = null
    private val binding get() = _binding!!

    val viewmodel: TranslateViewModel by viewModel()

    lateinit var searchLangDialog: SearchLangDialogFragment
    lateinit var searchLangDialogBinding: CustomViewSearchLanguageDialogBinding

    private val adapter by lazy { ConvoAdapter() }

    companion object {
        const val REQUEST_CODE_TTS_SOURCE = 11
        const val REQUEST_CODE_SPT_TARGET = 12
    }


    override fun initUI() {
        searchLangDialog = SearchLangDialogFragment()
        initLanguageContainerUI()
        setupBottomNav()
        initRvAndAdapter()
    }

    private fun initRvAndAdapter() {
        binding.rvConvo.apply {
            adapter = this@TranslateConvoFragment.adapter
            layoutManager = setLayoutManagerLinear()
        }

        adapter.setupAdapterInterface(object : ConvoAdapter.ItemInterface {
            override fun translate(model: ConvoModel, binding: ItemChatBinding) {
            }

            override fun speak(model: ConvoModel) {
                viewmodel.getTextToSpeechConvo(model)
            }
        })
    }

    private fun setupBottomNav() {
        binding.bottomNav.apply {
            setBottomMenuActive(tvConvo)
            setBottomMenuActive(ivConvo)
            btnMenuConvo.setOnClickListener {
                // Do Nothing
            }
            btnMenuTranslate.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_translateConvoFragment_to_navigation_translateFragment)
            }
        }
    }

    private fun checkMicUsingPermission(): Boolean {
        val dialogBuilder = AskPermissionDialog.Builder(requireContext())
        val checkAudio = getPermissionStatus(requireActivity(), PermissionUtil.PER_RECORD_AUDIO)
        if (checkAudio.isNotGranted()) {
            if (checkAudio.isBlocked()) {
                dialogBuilder.text("Penggunaan microphone diperlukan untuk menerjemahkan suara anda, sebelumnya anda telah memblokir penggunaan microphone, silakan izinkan secara otomatis di halaman pengaturan")
                    .positiveAction {
                        openSetting(requireContext())
                    }.build().show(binding.root)
            } else {
                dialogBuilder.text("Aplikasi membutuhkan microphone untuk menerjemahkan suara anda")
                    .positiveAction {
                        PermissionCommandUtil.micTranslate(requireActivity())
                    }.build().show(binding.root)
            }
            return false
        } else {
            // do nothing
            return true
        }
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
                    val containerInput = binding.containerInput

                    if (viewmodel.clickedContainer.value == 1) {
                        viewmodel.sourceLanguage.value = model.code
                        viewmodel.sourceLanguageDesc.value = model.name
                        containerInput.labelLanguageLeft.text = model.name
                    }

                    if (viewmodel.clickedContainer.value == 2) {
                        viewmodel.targetLanguage.value = model.code
                        viewmodel.targetLanguageDesc.value = model.name
                        containerInput.labelLanguageRight.text = model.name
                    }

                    searchLangDialog.dismiss()
                }
            })
        }
    }

    override fun initObserver() {

        viewmodel.translateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data?.let { translateResponse ->
                        if (translateResponse.status != 0) {
                            //if translation is success
                            val simpleTranslate = translateResponse.data.simple_translate
                            val simpleSpelling = translateResponse.data.spelling

                            val sourceLanguage = viewmodel.sourceLanguage.value
                            val targetLanguage = viewmodel.targetLanguage.value

                            val text = simpleTranslate

                            val convoModel = ConvoModel(
                                from = sourceLanguage.toString(),
                                to = targetLanguage.toString(),
                                text = viewmodel.textSpeaked.value.toString(),
                                result = text,
                                spelling = simpleSpelling,
                                type = viewmodel.clickedMicrophone.value.toString()
                            )

                            adapter.addData(convoModel)
                        }
                    }
                }
                else -> {}
            }
        }


        // observe lang desc
        val containerInput = binding.containerInput
        viewmodel.targetLanguageDesc.observe(viewLifecycleOwner) {
            containerInput.labelLanguageRight.text = it.toString()
        }
        viewmodel.sourceLanguageDesc.observe(viewLifecycleOwner) {
            containerInput.labelLanguageLeft.text = it.toString()
        }

        viewmodel.ttlLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data?.let { response ->
                        playMediaSource(response)
                    }
                }
                else -> {}
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

        microphoneClickedListener()
        handleTranslate()
        handleLanguageDropdownPickerClick()
    }

    private fun microphoneClickedListener() {
        binding.containerInput.apply {
            imgSpeakLeft.setOnClickListener {
                viewmodel.clickedMicrophone.value = 1
                proceedMicrophoneClicked("source")
            }

            imgSpeakRight.setOnClickListener {
                viewmodel.clickedMicrophone.value = 2
                proceedMicrophoneClicked("target")
            }
        }
    }

    private fun proceedMicrophoneClicked(type: String) {
        binding.containerInput.apply {
            val checkAudio = getPermissionStatus(requireActivity(), PermissionUtil.PER_RECORD_AUDIO)
            if (checkAudio.isNotGranted().not()) {
                getSpeechInput(type)
            } else {
                checkMicUsingPermission()
            }
        }
    }

    private fun handleLanguageDropdownPickerClick() {
        binding.containerInput.apply {
            arrayOf(labelLanguageLeft, imgDropdownLeft).forEachIndexed { index, view ->
                view.setOnClickListener {
                    viewmodel.clickedContainer.value = 1
                    searchLangDialog.show(getMFragmentManager(), "langPickerz")
                }
            }

            arrayOf(labelLanguageRight, imgDropdownRight).forEachIndexed { index, view ->
                view.setOnClickListener {
                    viewmodel.clickedContainer.value = 2
                    searchLangDialog.show(getMFragmentManager(), "langPickerz2")
                }
            }
        }
    }

    private fun getSpeechInput(type: String) {
        var requestCode = REQUEST_CODE_TTS_SOURCE
        val locale = if (type == "source") {
            requestCode = REQUEST_CODE_TTS_SOURCE
            viewmodel.sourceLanguage.value
        } else {
            requestCode = REQUEST_CODE_SPT_TARGET
            viewmodel.targetLanguage.value
        }

        val chooseLocale = TranslatorUtil.mapLocaleToSpeechToText(locale)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, chooseLocale)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, requestCode)
        }
    }

    private fun handleTranslate() {
        var timer = Timer()
        val DELAY: Long = 3000 // Milliseconds
        viewmodel.getTranslation("")
    }


    override fun initData() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTranslateConvoBinding.inflate(inflater)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                REQUEST_CODE_SPT_TARGET -> {
                    renderConvo("target", data)
                }
                REQUEST_CODE_TTS_SOURCE -> {
                    renderConvo("source", data)
                }
            }
        }

    }

    private fun renderConvo(type: String, data: Intent, targetLang: String = "") {
        val textView = binding.debug
        try {
            var result = ""
            data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let {
                result = it[0].toString()
            }
            viewmodel.textSpeaked.value = result
            viewmodel.getTranslation(result, type)
        } catch (e: Exception) {
            showSnackbar("Gagal Menerjemahkan Text")
        }
    }

}