package com.teleb.chefaaimagestask.data.models.response

import com.google.gson.annotations.SerializedName

data class ComicResponse
    (
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("attributionText")
    var attributionText: String,
    @SerializedName("data")
    var comicsData: ComicsDataModel,
    @SerializedName("status")
    var status: String
)

data class ComicsDataModel
    (
    @SerializedName("count")
    var count: Int,
    @SerializedName("limit")
    var limit: Int,
    @SerializedName("offset")
    var offset: Int,
    @SerializedName("results")
    var results: List<ComicsItemModel>,
    @SerializedName("total")
    var total: Int
)

data class ComicsItemModel
    (
    @SerializedName("id")
    var id: Int,
    @SerializedName("thumbnail")
    var thumbnail: ThumbnailModel,
    @SerializedName("title")
    var title: String
)

data class ThumbnailModel
    (
    @SerializedName("extension")
    private var extension: String,
    @SerializedName("path")
    private var path: String
)