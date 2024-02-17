package com.teleb.chefaaimagestask.domain.usecases

import android.util.Log
import com.teleb.chefaaimagestask.domain.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.UnknownHostException
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(
    private val getAllRemoteCharactersUseCase: GetAllRemoteCharactersUseCase,
    private val getAllLocalCharactersUseCase: GetAllLocalCharactersUseCase,
    private val saveAllCharactersUseCase: SaveAllCharactersUseCase,
) {

    // implementing caching algorithm
    suspend operator fun invoke() = flow {
        try {
            getAllRemoteCharactersUseCase.invoke()
                .catch { e ->
                    val characters = getAllLocalCharactersUseCase.invoke()
                    emit(Resource.Success(characters))

                    Log.i("vvv", "invoke: ${e.message}")
                }
                .collect {
                    when (it) {
                        is Resource.Success -> {

                                saveAllCharactersUseCase.invoke(it.data)
                                val characters = getAllLocalCharactersUseCase.invoke()

                                emit(Resource.Success(characters))

                        }

                        is Resource.Failed -> {

                            val characters = getAllLocalCharactersUseCase.invoke()
                            emit(Resource.Success(characters))
                        }
                    }
                }
        } catch (ex: UnknownHostException) {

            val characters = getAllLocalCharactersUseCase.invoke()
            emit(Resource.Success(characters))

        } catch (ex: Exception) {

            val characters = getAllLocalCharactersUseCase.invoke()
            emit(Resource.Success(characters))
        }
    }.flowOn(Dispatchers.IO)
}