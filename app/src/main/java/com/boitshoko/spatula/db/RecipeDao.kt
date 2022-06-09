package com.boitshoko.spatula.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.boitshoko.spatula.models.search.Result

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Result): Long

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): LiveData<List<Result>>

    @Delete
    suspend fun deleteRecipe(recipe: Result)
}