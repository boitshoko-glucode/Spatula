package com.boitshoko.spatula.repo

import com.boitshoko.spatula.api.RetrofitInstance
import com.boitshoko.spatula.db.RecipeDatabase
import com.boitshoko.spatula.models.search.Result

class RecipesRepo(
    private val db: RecipeDatabase
) {
    suspend fun searchRecipes(searchQuery: String) =
        RetrofitInstance.api.searchRecipes(searchQuery =  searchQuery)

    suspend fun getRecipeInstructions(id: Int) =
        RetrofitInstance.api.getRecipeInstructions(id =  id)

    suspend fun insertRecipe(recipe: Result) = db.getRecipeDao().insertFavourite(recipe)

    fun getSavedRecipes() = db.getRecipeDao().getAllFavourites()

    fun getRandomRecipe() = db.getRecipeDao().getRandomRecipe()

    suspend fun deleteRecipe(recipe: Result) = db.getRecipeDao().deleteRecipe(recipe)


}