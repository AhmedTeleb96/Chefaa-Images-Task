package com.teleb.chefaaimagestask.domain.usecases

import com.teleb.chefaaimagestask.data.models.ResizeRequest
import com.teleb.chefaaimagestask.domain.repositories.TinifyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class UpdateCharacterAndResizeImageUseCase @Inject constructor(private val repository: TinifyRepository) {

    fun executeRemoteDS(
        request: ResizeRequest
    ): Flow<File> = flow {

        val output = repository.shrinkImageFile(request.characterEntity, request.toMap())
        val file = repository.resizeImage(output, request.toMap())
        executeLocalOperation(file,request)
        emit(file)
    }

    private suspend fun executeLocalOperation(file: File, request: ResizeRequest) {
        repository.updateCurrentCharacter(request.characterEntity, file)
        file.delete()
        request.clear()
    }

}