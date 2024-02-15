package com.teleb.chefaaimagestask.domain.entities

data class CharacterEntity
(
    val id: Int,
    val name: String,
    val thumbnail: ThumbnailEntity,
    )

data class ThumbnailEntity
(
    val extension: String,
    val path: String,
    val fullPath: String
)