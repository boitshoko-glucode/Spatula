package com.boitshoko.spatula.api


import com.boitshoko.spatula.models.search.RecipesResponse
import com.boitshoko.spatula.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesAPI {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("query")
        searchQuery: String,
        @Query("number")
        number: Int = 4
    ): Response<RecipesResponse>
}