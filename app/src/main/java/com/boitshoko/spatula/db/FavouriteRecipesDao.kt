package com.boitshoko.spatula.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.boitshoko.spatula.db.models.RecipeStore
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteRecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(recipe: RecipeStore)

    @Query("SELECT * FROM recipes")
    fun getAllFavourites(): Flow<List<RecipeStore>>

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    fun getFavourite(id: Int): Flow<RecipeStore?>
}