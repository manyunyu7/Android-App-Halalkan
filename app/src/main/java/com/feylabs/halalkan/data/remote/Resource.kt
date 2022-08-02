package com.feylabs.halalkan.data.remote

sealed class MuskoResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>(data: T? = null) : MuskoResource<T>(data)
    class Success<T>(data: T, message: String = "") : MuskoResource<T>(data, message)
    class Error<T>(message: String, data: T? = null) : MuskoResource<T>(data, message)
    class Default<T>(data: T? = null) : MuskoResource<T>(data)
}

