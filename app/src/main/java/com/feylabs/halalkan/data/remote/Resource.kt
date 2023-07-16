package com.feylabs.halalkan.data.remote

sealed class QumparanResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>(data: T? = null) : QumparanResource<T>(data)
    class Success<T>(data: T, message: String = "") : QumparanResource<T>(data, message)
    class Error<T>(message: String="", data: T? = null) : QumparanResource<T>(data, message)
    class Default<T>(data: T? = null) : QumparanResource<T>(data)
}

