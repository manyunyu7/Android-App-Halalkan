package com.feylabs.halalkan.di

import com.feylabs.halalkan.data.QumparanRepository
import com.feylabs.halalkan.data.remote.service.ApiService
import com.feylabs.halalkan.utils.Network
import com.feylabs.halalkan.data.remote.RemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            /*
             * disabled for release
             .addInterceptor(
                 ChuckerInterceptor.Builder(androidContext())
                     .collector(ChuckerCollector(androidContext()))
                     .maxContentLength(250000L)
                     .redactHeaders(emptySet())
                     .alwaysReadResponseBody(true)
                     .build()
             )
             *
             */
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }


}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single { QumparanRepository(get()) }
}
