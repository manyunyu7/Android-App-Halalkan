package com.feylabs.halalkan.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ActivitySplashScreenBinding
import com.feylabs.halalkan.utils.base.BaseActivity
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {
    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        proceedNext()

        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                binding.apply {

                    logobm.makeGone()
                    tvCredit.makeGone()
                    tvCred.makeGone()

                    logobm.animation = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.item_animation_falldown_slowly
                    )
                    logobm.makeVisible()
                    tvCredit.animation = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.item_animation_falldown_slowly
                    )
                    tvCredit.makeVisible()
                    tvCred.animation = AnimationUtils.loadAnimation(
                        applicationContext, R.anim.item_animation_falldown_slowly
                    )
                    tvCred.makeVisible()
                }

            }

            delay(5000)
            withContext(Dispatchers.Main) {
                proceedNext()
            }
        }

    }

    private fun proceedNext() {
        finish()
        startActivity(Intent(this, ContainerActivity::class.java))
    }
}