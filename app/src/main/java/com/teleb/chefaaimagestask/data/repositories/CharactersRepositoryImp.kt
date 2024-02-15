package com.teleb.chefaaimagestask.data.repositories


import com.teleb.chefaaimagestask.BuildConfig
import com.teleb.chefaaimagestask.data.datasources.remote.ChefaaApiService
import com.teleb.chefaaimagestask.data.models.response.toDomainEntities
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import com.teleb.chefaaimagestask.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class CharactersRepositoryImp @Inject constructor(@Named("MarvelApi") private val apiService: ChefaaApiService) : CharactersRepository {

    override fun getAllCharacters(): Flow<Resource<List<CharacterEntity>>> =
        flow {
            try {
                val response = apiService.getCharacters()

                emit(Resource.Success(response.charactersData.results.toDomainEntities()))
            } catch (ex: Exception) {
                Resource.Failed<String>(ex.message ?: "Error")
            }

        }

}