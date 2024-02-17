package com.teleb.chefaaimagestask.data.repositories


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.teleb.chefaaimagestask.BuildConfig
import com.teleb.chefaaimagestask.data.datasources.db.CharactersDao
import com.teleb.chefaaimagestask.data.datasources.remote.ChefaaApiService
import com.teleb.chefaaimagestask.data.models.response.toDataModel
import com.teleb.chefaaimagestask.data.models.response.toDomainEntity
import com.teleb.chefaaimagestask.data.utils.ImageFileUtils
import com.teleb.chefaaimagestask.domain.entities.CharacterEntity
import com.teleb.chefaaimagestask.domain.entities.OutputEntity
import com.teleb.chefaaimagestask.domain.repositories.TinifyRepository
import okhttp3.RequestBody
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.inject.Inject
import javax.inject.Named

class TinyPngRepositoryImp @Inject constructor(
    @Named("TinifyApi") private val apiService: ChefaaApiService,
    private val charactersDao: CharactersDao,
    private val context: Context,
) : TinifyRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun shrinkImageFile(
        characterEntity: CharacterEntity,
        request: Map<String, Any?>
    ): OutputEntity {

        val authString = "api:${BuildConfig.TINIFY_API_KEY}".toByteArray(StandardCharsets.UTF_8)
        val auth = "Basic".plus(" ").plus(Base64.getEncoder().encodeToString(authString))

        val imageFileUrl = request["image_file_url"] as String

        val result = if (characterEntity.thumbnail.path.isNotEmpty()) apiService.shrinkUrlFile(
            auth, mapOf(
                "source" to mapOf(
                    "url" to imageFileUrl,
                )
            )
        )
        else apiService.shrinkLocalFile(auth, request["image_file"] as RequestBody)
        return result.output.toDomainEntity()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun resizeImage(output: OutputEntity, request: Map<String, Any?>): File {
        val authString = "api:${BuildConfig.TINIFY_API_KEY}".toByteArray(StandardCharsets.UTF_8)
        val auth = "Basic".plus(" ").plus(Base64.getEncoder().encodeToString(authString))

        val response = apiService.resizeImage(
            auth, output.url,
            mapOf(
                "resize" to mapOf(
                    "width" to request["width"],
                    "height" to request["height"],
                    "method" to "fit"
                )
            )
        )
        return ImageFileUtils(context).saveImageToFile(response)
    }

    override suspend fun updateCurrentCharacter(characterEntity: CharacterEntity, file: File) {
        characterEntity.thumbnail.apply {
            path = file.absolutePath
            setImageAsBitmap(path, context)
        }
        val dataModel = characterEntity.toDataModel()
        charactersDao.updateCharacter(dataModel)
    }


}