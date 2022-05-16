package com.feylabs.halalkan.di

import com.feylabs.halalkan.view.home.HomeViewModel
import com.feylabs.halalkan.view.postdetail.PostDetailViewModel
import com.feylabs.halalkan.view.prayer.PrayerRoomViewModel
import com.feylabs.halalkan.view.profile.UserProfileViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { PostDetailViewModel(get()) }
    viewModel { UserProfileViewModel(get()) }
    viewModel { PrayerRoomViewModel(get()) }
}

