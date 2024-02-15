package com.teleb.chefaaimagestask.di


import android.content.Context
import com.teleb.chefaaimagestask.data.datasources.db.CharactersDao
import com.teleb.chefaaimagestask.data.datasources.remote.ChefaaApiService
import com.teleb.chefaaimagestask.data.repositories.CharactersRepositoryImp
import com.teleb.chefaaimagestask.domain.repositories.CharactersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CharactersRepositoryModule {

    @Singleton
    @Provides
    fun provideCharactersRepository(@Named("MarvelApi") apiService: ChefaaApiService, charactersDao: CharactersDao,context: Context): CharactersRepository =
        CharactersRepositoryImp(apiService,charactersDao,context)
}