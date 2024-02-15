package com.teleb.chefaaimagestask.domain.usecases

import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {
     operator fun invoke( ) = charactersRepository.getAllCharacters()
}