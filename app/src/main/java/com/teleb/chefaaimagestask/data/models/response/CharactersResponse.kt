package com.teleb.chefaaimagestask.data.models.response

import com.google.gson.annotations.SerializedName
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.entities.ThumbnailEntity

data class CharactersResponse
    (
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("code")
    var code: Int? = null,
    @SerializedName("attributionText")
    var attributionText: String,
    @SerializedName("data")
    var charactersData: CharactersDataModel,
    @SerializedName("status")
    var status: String
)

data class CharactersDataModel
    (
    @SerializedName("count")
    var count: Int,
    @SerializedName("limit")
    var limit: Int,
    @SerializedName("offset")
    var offset: Int,
    @SerializedName("results")
    var results: List<CharactersItemModel>,
    @SerializedName("total")
    var total: Int
)

data class CharactersItemModel
    (
    @SerializedName("id")
    var id: Int,
    @SerializedName("thumbnail")
    var thumbnail: ThumbnailModel,
    @SerializedName("name")
    var name: String
)

data class ThumbnailModel
    (
    @SerializedName("extension")
     var extension: String,
    @SerializedName("path")
     var path: String
)

fun List<CharactersItemModel>.toDomainEntities() = map { CharacterEntity(it.id,it.name,it.thumbnail.toDomainEntity()) }

fun ThumbnailModel.toDomainEntity() = ThumbnailEntity(extension , path , "$path.$extension"  )