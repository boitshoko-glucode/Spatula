package com.boitshoko.spatula.repo

import com.boitshoko.spatula.api.RecipesAPI
import com.boitshoko.spatula.db.RecipeDao
import com.boitshoko.spatula.models.search.Result

class RecipesRepo(
    private val dao: RecipeDao,
    private val api: RecipesAPI
) {
    suspend fun searchRecipes(searchQuery: String) =
        api.searchRecipes(searchQuery =  searchQuery)

    suspend fun getRecipeInstructions(id: Int) =
        api.getRecipeInstructions(id =  id)

    suspend fun insertRecipe(recipe: Result) = dao.insertRecipe(recipe)

    fun getSavedRecipes() = dao.getAllRecipes()

    fun getRandomRecipe() = dao.getRandomRecipe()

    suspend fun deleteRecipe(recipe: Result) = dao.deleteRecipe(recipe)


}