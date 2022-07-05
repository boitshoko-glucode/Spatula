
package com.boitshoko.spatula.domain.gateways

import com.boitshoko.spatula.api.RecipesAPI
import com.boitshoko.spatula.domain.models.details.RecipeInstructions
import com.boitshoko.spatula.utils.Resource
import javax.inject.Inject

interface RecipeInstructionsGateway {
    suspend fun getRecipeInstructions(id: Int): Resource<RecipeInstructions>
}

class RecipeInstructionsGatewayImpl @Inject internal constructor(
    private val api: RecipesAPI
): RecipeInstructionsGateway {
    override suspend fun getRecipeInstructions(id: Int): Resource<RecipeInstructions> {
        val response = api.getRecipeInstructions(id = id)

        if (response.isSuccessful){
            response.body()?.let {
              //  val entities = resultResponse.results.map { Recipe(it) }
                val entity = RecipeInstructions(it[0].steps)
                return Resource.Success(entity)
            }
        }

        return Resource.Error(response.message())
    }
}
