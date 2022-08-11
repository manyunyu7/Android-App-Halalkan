package com.feylabs.halalkan.di

import android.content.Context
import com.feylabs.halalkan.data.local.MyPreference
import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor(private val context:Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = MyPreference(context).getToken()

        request = request.newBuilder()
            .addHeader("Authorization",token)
            .build()

        return chain.proceed(request)
    }
}