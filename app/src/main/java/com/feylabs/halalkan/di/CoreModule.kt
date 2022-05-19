package com.feylabs.halalkan.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.halalkan.data.QumparanRepository
import com.feylabs.halalkan.data.TranslatorRepository
import com.feylabs.halalkan.data.remote.service.ApiService
import com.feylabs.halalkan.utils.Network
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.service.MasjidService
import com.feylabs.halalkan.data.remote.service.TranslatorService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(
                ChuckerInterceptor.Builder(androidContext())
                    .collector(ChuckerCollector(androidContext()))
                    .maxContentLength(250000L)
                    .redactHeaders(emptySet())
                    .alwaysReadResponseBody(true)
                    .build()
            )

            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single<ApiService>(named("mainService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }

    single<TranslatorService>(named("translatorService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_TRANSLATOR)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(TranslatorService::class.java)
    }


}

val repositoryModule = module {
    single { RemoteDataSource(get(named("mainService")), get(named("translatorService"))) }
    single { QumparanRepository(get()) }
    single { TranslatorRepository(get()) }
}
