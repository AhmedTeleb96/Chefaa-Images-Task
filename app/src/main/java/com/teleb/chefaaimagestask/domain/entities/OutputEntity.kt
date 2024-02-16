package com.teleb.chefaaimagestask.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OutputEntity(
    var height: Float,
    var url: String,
    var width: Float
) : Parcelable