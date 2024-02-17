package com.teleb.chefaaimagestask.data.repositories


import android.content.Context
import android.util.Log
import com.teleb.chefaaimagestask.data.datasources.db.CharactersDao
import com.teleb.chefaaimagestask.data.datasources.remote.ChefaaApiService
import com.teleb.chefaaimagestask.data.models.response.toDataModel
import com.teleb.chefaaimagestask.data.models.response.toDataModels
import com.teleb.chefaaimagestask.data.models.response.toDomainEntities
import com.teleb.chefaaimagestask.data.models.response.toDomainEntity
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import com.teleb.chefaaimagestask.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class CharactersRepositoryImp @Inject constructor(
    @Named("MarvelApi") private val apiService: ChefaaApiService,
    private val charactersDao: CharactersDao,
    private val context: Context,
    ) : CharactersRepository {

    override fun getAllCharacters(): Flow<Resource<List<CharacterEntity>>> =
        flow {
                val response = apiService.getCharacters()

                emit(Resource.Success(response.charactersData.results.toDomainEntities(context)))
        }

    override suspend fun getCharactersLocal(): List<CharacterEntity> {
            val result = charactersDao.getAllCharacters()
            return result.toDomainEntities(context).reversed()
    }

    override suspend fun getCharacterById(id: Int): Flow<CharacterEntity> {
        val result = charactersDao.getCharacterById(id)
        return result.map {
           it.toDomainEntity(context)
        }
    }

    override suspend fun saveCharactersList(characterItems: List<CharacterEntity>) {

        charactersDao.insertAllCharacters(characterItems.toDataModels())
    }

    override suspend fun saveCharacterItem(characterItem: CharacterEntity) {
        charactersDao.insert(characterItem.toDataModel())
    }

    override suspend fun updateCharacterItem(characterItem: CharacterEntity) {

        charactersDao.updateCharacter(characterItem.toDataModel())
    }
    override suspend fun clearDatabase() {

        charactersDao.clearDatabase()
    }
}