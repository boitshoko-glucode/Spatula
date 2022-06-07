package com.boitshoko.spatula.repo

import com.boitshoko.spatula.api.RetrofitInstance

class RecipesRepo {
    suspend fun searchRecipes(searchQuery: String) =
        RetrofitInstance.api.searchRecipes(searchQuery =  searchQuery)


}