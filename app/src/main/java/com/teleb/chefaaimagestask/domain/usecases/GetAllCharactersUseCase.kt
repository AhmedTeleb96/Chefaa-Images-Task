package com.teleb.chefaaimagestask.domain.usecases

import com.teleb.chefaaimagestask.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.UnknownHostException
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(
    private val getAllRemoteCharactersUseCase: GetAllRemoteCharactersUseCase,
    private val getAllLocalCharactersUseCase: GetAllLocalCharactersUseCase,
    private val saveAllCharactersUseCase: SaveAllCharactersUseCase,
    private val deleteAllCharactersUseCase: DeleteAllCharactersUseCase,
) {

    // implementing caching algorithm
    suspend operator fun invoke() = flow {
        try {
            getAllRemoteCharactersUseCase.invoke().collect {
                when (it) {
                    is Resource.Success -> {
                        deleteAllCharactersUseCase.invoke()
                        saveAllCharactersUseCase.invoke(it.data)

                        emit(Resource.Success(it.data))
                    }

                    is Resource.Failed -> {

                        val characters = getAllLocalCharactersUseCase.invoke()

                        emit(Resource.Success(characters))
                    }
                }
            }
        }
        catch (ex: UnknownHostException) {

            val characters = getAllLocalCharactersUseCase.invoke()

            emit(Resource.Success(characters))

        }
        catch (ex: Exception) {
            emit(Resource.Failed(ex.message.toString()))
        }
    }.flowOn(Dispatchers.IO)
}