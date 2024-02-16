package com.teleb.chefaaimagestask.data.models.response

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.annotations.SerializedName
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.entities.ThumbnailEntity
import com.teleb.chefaaimagestask.domain.utils.toHttps
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0,
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
    var path: String,
    var imageBytes: ByteArray?,
) {
    fun getBitmap(): Bitmap? {
        return if (imageBytes != null ) BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes?.size ?: 0) else null
    }

    suspend fun setImageAsBitmap(path: String, context: Context) =
        suspendCoroutine { continuation ->
            Glide.with(context).asBitmap()
                .load(path)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap, transition: Transition<in Bitmap>?
                    ) {
                        imageBytes = encodeToByteArray(resource)
                        continuation.resume(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })
        }

    fun encodeToByteArray(resource: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        resource.compress(getCompressFormat(), 100, stream)
        return stream.toByteArray()
    }
    private fun getCompressFormat() = when (path.substringAfter(".")) {
        "jpg" -> Bitmap.CompressFormat.JPEG
        else -> Bitmap.CompressFormat.PNG
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ThumbnailModel) return false

        return imageBytes.contentEquals(other.imageBytes)
    }

    override fun hashCode(): Int {
        return imageBytes.contentHashCode()
    }
}

suspend fun List<CharactersItemModel>.toDomainEntities(context: Context) =
    map { CharacterEntity(it.id, it.name, it.thumbnail.toDomainEntity(context)) }

fun List<CharacterEntity>.toDataModels() =
    map { CharactersItemModel(it.id, it.thumbnail.toDataModel(), it.name) }

suspend fun CharactersItemModel.toDomainEntity(context: Context) =
    CharacterEntity(id, name, thumbnail.toDomainEntity(context))

fun CharacterEntity.toDataModel() = CharactersItemModel(id, thumbnail.toDataModel(), name)
suspend fun ThumbnailModel.toDomainEntity(context: Context) = ThumbnailEntity(
    extension,
    path,
    "$path.$extension".toHttps(),
    setImageAsBitmap("$path.$extension".toHttps(), context)
)

fun ThumbnailEntity.toDataModel() = ThumbnailModel(extension, path,encodeToByteArray())