package com.feylabs.halalkan.data

import com.feylabs.halalkan.data.remote.RemoteDataSource

class TranslatorRepository(
    private val remoteDs: RemoteDataSource,
) {

    suspend fun getTranslation(
        source: String, target: String, text: String
    ) = remoteDs.getTranslation(
        source, target = target, text = text
    )

    suspend fun getTextToSpeech(
        source: String, text: String
    ) = remoteDs.getTextToSpeech(langSource = source, text = text)

}