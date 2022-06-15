package com.boitshoko.spatula.domain.gateways

import com.boitshoko.spatula.api.RecipesAPI
import com.boitshoko.spatula.domain.models.Recipe
import com.boitshoko.spatula.utils.Resource
import javax.inject.Inject

interface RecipesGateway {
    suspend fun getRecipes(query: String): Resource<List<Recipe>>
}

class RecipesGatewayImpl @Inject internal constructor(
    private val api: RecipesAPI
): RecipesGateway {
    override suspend fun getRecipes(query: String): Resource<List<Recipe>> {
        val response = api.searchRecipes(searchQuery = query)

        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                val entities = resultResponse.results.map { Recipe(it) }
                return Resource.Success(entities)
            }
        }

        return Resource.Error(response.message())
    }
}