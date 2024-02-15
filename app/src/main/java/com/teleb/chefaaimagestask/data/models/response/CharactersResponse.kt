package com.teleb.chefaaimagestask.data.models.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.entities.ThumbnailEntity
import com.teleb.chefaaimagestask.domain.utils.toHttps
import okhttp3.HttpUrl.Companion.toHttpUrl

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

@Entity
data class CharactersItemModel
    (
    @PrimaryKey
    @SerializedName("id")
    var id: Int,
    @Embedded
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
fun List<CharacterEntity>.toDataModels() = map { CharactersItemModel(it.id,it.thumbnail.toDataModel(),it.name) }
fun CharactersItemModel.toDomainEntity() = CharacterEntity(id,name,thumbnail.toDomainEntity())
fun CharacterEntity.toDataModel() = CharactersItemModel(id,thumbnail.toDataModel(),name)
fun ThumbnailModel.toDomainEntity() = ThumbnailEntity(extension , path , "$path.$extension".toHttps()  )
fun ThumbnailEntity.toDataModel() = ThumbnailModel(extension , path )