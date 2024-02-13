package com.teleb.chefaaimagestask.data.datasources.remote

import com.teleb.chefaaimagestask.data.models.response.ComicResponse
import com.teleb.chefaaimagestask.data.models.response.TinfyResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ChefaaApiService
{
    @GET("public/comics")
    suspend fun getComics(@QueryMap hashMap: HashMap<String, Any>): ComicResponse

    @POST("https://api.tinify.com/shrink")
    suspend fun shrinkLocalFile(
        @Body file: RequestBody,
    ): TinfyResponse

    @POST("https://api.tinify.com/shrink")
    suspend fun shrinkUrlFile(
        @Body body: HashMap<String, Any>,
    ): TinfyResponse

    @POST("{pathUrl}")
    @JvmSuppressWildcards
    suspend fun resizeImage(
        @Path(value = "pathUrl", encoded = true) pathUrl: String,
        @Body body: HashMap<String, Any>
    ): ResponseBody
}