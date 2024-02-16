package com.teleb.chefaaimagestask.data.datasources.remote

import com.teleb.chefaaimagestask.data.models.response.CharactersResponse
import com.teleb.chefaaimagestask.data.models.response.TinfyResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChefaaApiService
{
    @GET("public/characters")
    suspend fun getCharacters(): CharactersResponse

    @POST("https://api.tinify.com/shrink")
    suspend fun shrinkLocalFile(
        @Header("Authorization") auth:String,
        @Body file: RequestBody,
    ): TinfyResponse

    @POST("https://api.tinify.com/shrink")
    @JvmSuppressWildcards
    suspend fun shrinkUrlFile(
        @Header("Authorization") auth:String,
        @Body body: Map<String, Any?>,
    ): TinfyResponse

    @POST("{pathUrl}")
    @JvmSuppressWildcards
    suspend fun resizeImage(
        @Header("Authorization") auth:String,
        @Path(value = "pathUrl", encoded = true) pathUrl: String,
        @Body body: Map<String, Any?>
    ): ResponseBody
}