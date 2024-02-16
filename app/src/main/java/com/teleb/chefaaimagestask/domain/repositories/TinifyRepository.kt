package com.teleb.chefaaimagestask.domain.repositories

import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.entities.OutputEntity
import java.io.File

interface TinifyRepository {
    suspend fun shrinkImageFile(characterEntity: CharacterEntity, request: Map<String, Any?>): OutputEntity
    suspend fun resizeImage(output: OutputEntity, request: Map<String, Any?>): File
    suspend fun updateCurrentCharacter(characterEntity: CharacterEntity, file: File)
}