package com.feylabs.halalkan.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.halalkan.R
import com.feylabs.halalkan.utils.Network.REAL_URL
import com.feylabs.halalkan.utils.StringUtil.encodeUrl
import com.pixplicity.sharp.Sharp
import okhttp3.*
import java.io.IOException

object ImageViewUtils {

    fun String.urlHalal(){
        var url = this.encodeUrl()
        if(url.contains("/"))
        url = REAL_URL+url
    }

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

    enum class ThumbnailsType(val value: Int) {
        ADD_PHOTO_1(R.drawable.bg_header_daylight),
        LOADING_1(R.drawable.bg_header_daylight),
    }

    fun ImageView.loadImageFromURL(
        context: Context,
        url: String? = "",
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(url)
            .placeholder(thumbnailsType.value)
//            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

    fun ImageView.loadImage(
        context: Context,
        drawable: Int,
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        Glide.with(context)
            .load(drawable)
            .placeholder(thumbnailsType.value)
            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

}