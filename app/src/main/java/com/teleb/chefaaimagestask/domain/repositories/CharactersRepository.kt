package com.teleb.chefaaimagestask.domain.repositories

import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getAllCharacters() : Flow<Resource<List<CharacterEntity>>>
    suspend fun getCharactersLocal(): List<CharacterEntity>
    suspend fun getCharacterById(id: Int): Flow<CharacterEntity>
    suspend fun saveCharactersList(characterItems: List<CharacterEntity>)
    suspend fun saveCharacterItem(characterItem: CharacterEntity)
    suspend fun updateCharacterItem(characterItem: CharacterEntity)
    suspend fun clearDatabase()
}