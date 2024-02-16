package com.teleb.chefaaimagestask.di


import android.content.Context
import com.teleb.chefaaimagestask.data.datasources.db.CharactersDao
import com.teleb.chefaaimagestask.data.datasources.remote.ChefaaApiService
import com.teleb.chefaaimagestask.data.repositories.TinyPngRepositoryImp
import com.teleb.chefaaimagestask.domain.repositories.TinifyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TinifyRepositoryModule {

    @Singleton
    @Provides
    fun provideCharactersRepository(@Named("TinifyApi") apiService: ChefaaApiService, charactersDao: CharactersDao,context: Context): TinifyRepository =
        TinyPngRepositoryImp(apiService,charactersDao,context)
}