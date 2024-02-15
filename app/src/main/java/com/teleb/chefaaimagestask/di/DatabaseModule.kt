package com.teleb.chefaaimagestask.di

import android.app.Application
import com.teleb.chefaaimagestask.data.datasources.db.CharactersDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule{

    @Singleton
    @Provides
    fun provideDatabase(application: Application) = CharactersDataBase.invoke(application)

    @Singleton
    @Provides
    fun provideCharactersDao(charactersDataBase: CharactersDataBase) = charactersDataBase.getCharactersDao()
}
