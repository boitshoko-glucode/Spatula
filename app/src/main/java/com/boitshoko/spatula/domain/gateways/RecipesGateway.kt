package com.boitshoko.spatula.domain.gateways

import com.boitshoko.spatula.api.RecipesAPI
import com.boitshoko.spatula.domain.models.Recipe
import com.boitshoko.spatula.utils.Resource

interface RecipesGateway {
    suspend fun getRecipes(query: String): Resource<List<Recipe>>
}

class RecipesGatewayImpl(
    private val api: RecipesAPI
): RecipesGateway {
    override suspend fun getRecipes(query: String): Resource<List<Recipe>> {
        val response = api.searchRecipes(searchQuery = query)

        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                val entities = resultResponse.results.map {
                    Recipe(name = it.title)
                }
                return Resource.Success(entities)
            }
        }

        return Resource.Error(response.message())
    }
}