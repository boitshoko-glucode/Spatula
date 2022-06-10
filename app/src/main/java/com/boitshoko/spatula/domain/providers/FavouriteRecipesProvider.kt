package com.boitshoko.spatula.domain.providers

import com.boitshoko.spatula.domain.models.Recipe
import kotlinx.coroutines.flow.Flow

interface FavouriteRecipesProvider {
    val favourites: Flow<List<Recipe>>
}