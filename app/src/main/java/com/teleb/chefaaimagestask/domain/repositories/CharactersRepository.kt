package com.teleb.chefaaimagestask.domain.repositories

import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    fun getAllCharacters() : Flow<Resource<List<CharacterEntity>>>
}