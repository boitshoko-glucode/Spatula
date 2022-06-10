package com.boitshoko.spatula.domain.usecases

import com.boitshoko.spatula.domain.gateways.RecipesGateway

interface RecipeFinder {
    suspend fun find(name: String)
}

class RecipeFinderImpl(
    private val gateway: RecipesGateway
): RecipeFinder {
    override suspend fun find(name: String) {
        gateway.getRecipes(name)
    }
}