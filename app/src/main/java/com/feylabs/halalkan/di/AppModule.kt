package com.feylabs.halalkan.di

import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.view.resto.admin_resto.AdminRestoViewModel
import com.feylabs.halalkan.view.auth.AuthViewModel
import com.feylabs.halalkan.view.direction.DirectionViewModel
import com.feylabs.halalkan.view.favorite.FavViewModel
import com.feylabs.halalkan.view.forum.ForumViewModel
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.postdetail.PostDetailViewModel
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import com.feylabs.halalkan.view.prayer.review.PrayerRoomReviewViewModel
import com.feylabs.halalkan.view.prayer.review.RestoReviewViewModel
import com.feylabs.halalkan.view.products.ProductViewModel
import com.feylabs.halalkan.view.resto.DriverViewModel
import com.feylabs.halalkan.view.resto.OrderViewModel
import com.feylabs.halalkan.view.resto.RestoViewModel
import com.feylabs.halalkan.view.translate.TranslateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(),get(),get()) }

    viewModel { AuthViewModel(get(),get()) }
    viewModel { DriverViewModel(get()) }

    viewModel { PostDetailViewModel(get()) }

    viewModel { RestoViewModel(get()) }
    viewModel { RestoReviewViewModel(get()) }
    viewModel { AdminRestoViewModel(get()) }

    viewModel { PrayerRoomViewModel(get(),get(),get(),get()) }
    viewModel { PrayerRoomReviewViewModel(get()) }

    viewModel { TranslateViewModel(get()) }

    viewModel { DirectionViewModel(get()) }
    viewModel { ForumViewModel(get()) }
    viewModel { OrderViewModel(get()) }
    viewModel { ProductViewModel(get()) }
    viewModel { FavViewModel(get()) }
}

