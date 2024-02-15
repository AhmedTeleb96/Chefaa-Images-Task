package com.teleb.chefaaimagestask.domain.usecases

import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import javax.inject.Inject

class GetAllLocalCharactersUseCase @Inject constructor(private val charactersRepository: CharactersRepository) {
     suspend operator fun invoke( ) = charactersRepository.getCharactersLocal()
}