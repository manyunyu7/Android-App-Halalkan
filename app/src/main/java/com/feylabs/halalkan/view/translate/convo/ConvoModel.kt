package com.feylabs.halalkan.view.translate.convo

data class ConvoModel(
    val timestamp:String="",
    val from:String,
    val to :String,
    var text : String,
    var spelling : String? = null,
    var result:String,
    var type :String
)
