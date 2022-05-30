package com.feylabs.halalkan.di

import com.feylabs.halalkan.MainViewModel
import com.feylabs.halalkan.view.direction.DirectionViewModel
import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.postdetail.PostDetailViewModel
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import com.feylabs.halalkan.view.profile.UserProfileViewModel
import com.feylabs.halalkan.view.translate.TranslateViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { MainViewModel() }
    viewModel { HomeViewModel(get(),get(),get()) }

    viewModel { PostDetailViewModel(get()) }

    viewModel { UserProfileViewModel(get()) }

    viewModel { PrayerRoomViewModel(get(),get(),get(),get()) }

    viewModel { TranslateViewModel(get()) }

    viewModel { DirectionViewModel(get()) }
}

