package com.feylabs.halalkan.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.feylabs.halalkan.utils.Network
import com.feylabs.halalkan.data.remote.RemoteDataSource
import com.feylabs.halalkan.data.remote.service.*
import com.feylabs.halalkan.data.repository.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
            .addInterceptor(ServiceInterceptor(androidContext()))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single<ApiService>(named("mainService")) {
        val retrofit =  Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }

    single<RestoService>(named("restoService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(RestoService::class.java)
    }

    single<ForumService>(named("forumService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ForumService::class.java)
    }

    single<MasjidService>(named("masjidService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(MasjidService::class.java)
    }

    single<TranslatorService>(named("translatorService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_TRANSLATOR)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(TranslatorService::class.java)
    }

    single<PrayerTimeAladhanService>(named("prayerTimeAladhanService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_ALADHAN_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(PrayerTimeAladhanService::class.java)
    }

    single<DriverService>(named("driverService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(DriverService::class.java)
    }

    single<FavoriteService>(named("favService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(FavoriteService::class.java)
    }

    single<ProductService>(named("productService")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Network.BASE_URL_V1)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ProductService::class.java)
    }


}

val repositoryModule = module {
    single {
        RemoteDataSource(
            get(named("mainService")),
            get(named("masjidService")),
            get(named("translatorService")),
            get(named("prayerTimeAladhanService")),
            get(named("restoService")),
            get(named("favService")),
            get(named("forumService")),
            get(named("driverService")),
            get(named("productService")),
        )
    }
    single { MasjidRepository(get()) }
    single { QumparanRepository(get()) }
    single { TranslatorRepository(get()) }
    single { PrayerTimeRepository(get()) }
    single { FavoriteRepository(get()) }
}
