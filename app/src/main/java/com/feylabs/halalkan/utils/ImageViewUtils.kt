package com.feylabs.halalkan.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feylabs.halalkan.R
import com.feylabs.halalkan.utils.Network.REAL_URL
import com.feylabs.halalkan.utils.StringUtil.decodeMuskoUrl
import com.pixplicity.sharp.Sharp
import okhttp3.*
import java.io.File
import java.io.IOException

object ImageViewUtils {

    fun String.urlHalal() {
        var url = this.decodeMuskoUrl()
        if (url.contains("/"))
            url = REAL_URL + url
    }

    fun String.imgFullPath() = Network.STORAGE_V1 + this

    fun String.imgFullUserPath() =
        REAL_URL + "uploads/img/users/$this"

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
            .placeholder(loadThumbnails())
//            .thumbnail(Glide.with(context).load(R.raw.ic_loading_google).fitCenter())
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(this)
    }

    private fun loadThumbnails(): Int {
        val list = listOf(
//            R.drawable.bg_header_daylight,
//            R.drawable.bg_header_dawn,
//            R.drawable.bg_header_evening,
//            R.drawable.bg_header_night,
            R.drawable.placeholder,
            R.drawable.white_image_placeholder,
//            R.drawable.bg_header_sunrise
        )
        return list.random()
    }

    fun ImageView.loadImage(
        context: Context,
        uri: Uri? = null,
        file: File? = null,
        thumbnailsType: ThumbnailsType = ThumbnailsType.LOADING_1
    ) {
        if (file != null) {
            Glide.with(context)
                .load(file)
                .placeholder(thumbnailsType.value)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(this)
        } else {
            if (uri != null) {
                Glide.with(context)
                    .load(uri)
                    .placeholder(thumbnailsType.value)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(this)
            }
        }

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