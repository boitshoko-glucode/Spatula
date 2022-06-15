package com.boitshoko.spatula.domain.usecases

import com.boitshoko.spatula.domain.gateways.RecipesGateway
import com.boitshoko.spatula.domain.models.Recipe
import com.boitshoko.spatula.utils.Resource
import javax.inject.Inject

interface RecipeFinder {
    suspend fun find(name: String): Resource<List<Recipe>>
}

class RecipeFinderImpl @Inject internal constructor(
    private val gateway: RecipesGateway
): RecipeFinder {
    override suspend fun find(name: String): Resource<List<Recipe>> = gateway.getRecipes(name)
}