package com.teleb.chefaaimagestask.data.models.response

import com.google.gson.annotations.SerializedName

data class TinfyResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("output")
    var output: OutputModel
)

data class OutputModel(
@SerializedName("height")
var height: Float,
@SerializedName("url")
var url: String,
@SerializedName("width")
var width: Float
)