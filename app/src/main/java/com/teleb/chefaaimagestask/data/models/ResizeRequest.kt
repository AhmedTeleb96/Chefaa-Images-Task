package com.teleb.chefaaimagestask.data.models

import com.teleb.chefaaimagestask.data.utils.deleteIfExists
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

data class ResizeRequest(
    val imageFilePath: String,
    val height: Float, val width: Float,
    val characterEntity: CharacterEntity
)
{
    fun toMap() = mapOf(
        "image_file" to  File(imageFilePath).createRequestBody() ,"height" to height,"width" to width,"image_file_url" to characterEntity.thumbnail.fullPath
    )

    private fun File.createRequestBody(type: String = "image/*"): RequestBody {
        return asRequestBody(type.toMediaTypeOrNull())
    }

    fun clear() {
        try {
            File(imageFilePath).deleteIfExists()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
