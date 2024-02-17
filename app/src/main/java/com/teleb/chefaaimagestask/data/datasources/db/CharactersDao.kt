package com.teleb.chefaaimagestask.data.datasources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teleb.chefaaimagestask.data.models.response.CharactersItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(character: CharactersItemModel): Long

    @Query("SELECT * FROM CharactersItemModel")
    suspend fun getAllCharacters(): List<CharactersItemModel>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCharacters(characterItems: List<CharactersItemModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCharacter(characterItem: CharactersItemModel)

    @Query("SELECT * FROM CharactersItemModel where id=:id")
    fun getCharacterById(id: Int): Flow<CharactersItemModel>


    @Query("DELETE FROM CharactersItemModel")
    suspend fun clearDatabase()


}
