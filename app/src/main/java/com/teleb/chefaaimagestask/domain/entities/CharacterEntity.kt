package com.teleb.chefaaimagestask.domain.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Parcelable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Parcelize
data class CharacterEntity
(
    val id: Int,
    val name: String,
    val thumbnail: ThumbnailEntity,
    ) : Parcelable

@Parcelize
data class ThumbnailEntity
(
    val extension: String,
    val path: String,
    val fullPath: String,
    var imageBitmap: Bitmap? = null,
) : Parcelable {

}