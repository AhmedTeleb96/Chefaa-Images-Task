package com.teleb.chefaaimagestask.di

import com.google.gson.GsonBuilder
import com.teleb.chefaaimagestask.BuildConfig
import com.teleb.chefaaimagestask.data.datasources.remote.ChefaaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    @Named("MarvelApi")
    fun provideChefaaApiServiceMarvel(@Named("Marvel_Retrofit") retrofit: Retrofit): ChefaaApiService =
        retrofit.create(ChefaaApiService::class.java)
    @Provides
    @Singleton
    @Named("TinifyApi")
    fun provideChefaaApiServiceTinify(@Named("Tinify_Retrofit") retrofit: Retrofit): ChefaaApiService =
        retrofit.create(ChefaaApiService::class.java)


    @Provides
    @Singleton
    @Named("Marvel_Retrofit")
    fun provideRetrofitMarvel(
        gsonFactory: GsonConverterFactory,
        @Named("MARVEL") okHttpClient: OkHttpClient.Builder,
    ): Retrofit =
        Retrofit.Builder().client(okHttpClient.build())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(gsonFactory).build()
    @Provides
    @Singleton
    @Named("Tinify_Retrofit")
    fun provideRetrofitTinify(
        gsonFactory: GsonConverterFactory,
        @Named("TINIFY") okHttpClient: OkHttpClient.Builder,
    ): Retrofit =
        Retrofit.Builder().client(okHttpClient.build())
            .addConverterFactory(gsonFactory).build()

    @Provides
    @Singleton
    fun provideGsonFactory(): GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().create())

    @Provides
    @Singleton
    @Named("TINIFY")
    fun provideOkHttpClientTinify(interceptor: HttpLoggingInterceptor) =
        OkHttpClient().newBuilder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader(
                    "Authorization",
                    "api:${BuildConfig.TINIFY_API_KEY}"
                )
                .build()
            chain.proceed(newRequest)

        }.apply {
            connectTimeout(30L, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            readTimeout(30L, TimeUnit.SECONDS)
            writeTimeout(30L, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            addInterceptor(getHttpLoggingInterceptor())
        }

    @Provides
    @Singleton
    @Named("MARVEL")
    fun provideOkHttpClientMarvel(interceptor: HttpLoggingInterceptor) =
        OkHttpClient().newBuilder().apply {
            connectTimeout(30L, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            readTimeout(30L, TimeUnit.SECONDS)
            writeTimeout(30L, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            addInterceptor(getHttpLoggingInterceptor())
        }

    @Provides
    @Singleton
    fun getHttpLoggingInterceptor():HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
}