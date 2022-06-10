package com.boitshoko.spatula.domain.cache

import com.boitshoko.spatula.db.FavouriteRecipesDao
import com.boitshoko.spatula.db.models.RecipeStore
import com.boitshoko.spatula.domain.models.Recipe
import com.boitshoko.spatula.domain.providers.FavouriteRecipesProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FavouriteRecipesCache: FavouriteRecipesProvider {
    fun get(id: Int): Flow<Recipe?>
    suspend fun insert(recipe: Recipe)
}

class FavouriteRecipesCacheImpl(
    private val dao: FavouriteRecipesDao
): FavouriteRecipesCache {
    override val favourites: Flow<List<Recipe>> = dao.getAllFavourites().map { stores ->
        stores.map {
            Recipe(name = it.name)
        }
    }

    override fun get(id: Int): Flow<Recipe?> = dao.getFavourite(id).map { store ->
        store?.let { Recipe(it.name) }
    }

    override suspend fun insert(recipe: Recipe) {
        val store = RecipeStore(1, "", "", true)
        dao.insertFavourite(store)
    }
}