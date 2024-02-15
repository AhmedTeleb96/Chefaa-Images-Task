package com.teleb.chefaaimagestask.domain.usecases

import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import javax.inject.Inject

class SaveAllCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {
    suspend operator fun invoke(characterItems: List<CharacterEntity>) =
        charactersRepository.saveCharactersList(characterItems)
}