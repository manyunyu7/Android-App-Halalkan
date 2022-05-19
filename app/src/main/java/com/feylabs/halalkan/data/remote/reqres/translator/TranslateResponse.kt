package com.feylabs.halalkan.data.remote.reqres.translator

data class TranslateResponse(
    val `data`: Data,
    val message: String,
    val status: Int
){
    data class Data(
        val simple_translate: String,
        val source: String,
        val spelling: String,
        val target: String,
        val text: String
    )
}