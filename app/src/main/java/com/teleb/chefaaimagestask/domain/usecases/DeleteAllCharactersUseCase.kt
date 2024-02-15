package com.teleb.chefaaimagestask.domain.usecases

import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import javax.inject.Inject

class DeleteAllCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {
    suspend operator fun invoke() = charactersRepository.clearDatabase()
}