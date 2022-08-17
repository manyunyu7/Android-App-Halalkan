package com.feylabs.halalkan.data.remote.reqres.forum


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

class ForumCategoryResponse : ArrayList<ForumCategoryResponse.ForumCategoryResponseItem>(){
    @Keep
    data class ForumCategoryResponseItem(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String = ""
    )
}