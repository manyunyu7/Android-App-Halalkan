package com.feylabs.halalkan.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ActivityContainerBinding
import com.feylabs.halalkan.utils.base.BaseActivity
import java.util.*


class ContainerActivity : BaseActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityContainerBinding
    private var tts: TextToSpeech? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this,this)
        checkTTSAvailability()

        // val navView: BottomNavigationView = binding.navView
        // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val navController = findNavController(R.id.nav_host_fragment_activity_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_newHomeFragment,
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
    }

    private fun checkTTSAvailability() {
        val installIntent = Intent()
        installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
        startActivity(installIntent)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.KOREA
        } else {
            showToast("Device Not Supported")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    fun speakOut(text: String,code:String) {
        showToast("speak out $code ok")
        var lang = Locale("id","ID")
        tts?.language = Locale.KOREA
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    fun isPackageInstalled(pm: PackageManager, packageName: String?): Boolean {
        try {
            pm.getPackageInfo(packageName!!, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    /* Check is there a NetworkConnection */
    protected fun internetIsConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm?.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}