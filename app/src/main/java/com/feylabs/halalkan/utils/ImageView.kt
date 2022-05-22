package com.feylabs.halalkan.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.feylabs.halalkan.R
import com.pixplicity.sharp.Sharp
import okhttp3.*
import java.io.IOException

object ImageViewUtils {
    fun ImageView.loadSvg(context: Context, url: String) {
        val imageView = this
        val httpClient = OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, 5 * 1024 * 1014))
            .build()
        val request: Request = Request.Builder().url(url).build()
        httpClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                imageView.setImageResource(R.drawable.ic_example_profile)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                response.body?.apply {
                    val stream = this.byteStream()
                    Sharp.loadInputStream(stream).into(imageView)
                    stream.close()
                }
            }
        })
    }
}