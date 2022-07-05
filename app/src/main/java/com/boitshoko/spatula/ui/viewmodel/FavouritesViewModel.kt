package com.boitshoko.spatula.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.boitshoko.spatula.domain.cache.FavouriteRecipesCache
import com.boitshoko.spatula.domain.models.Recipe
import com.boitshoko.spatula.domain.usecases.RecipeFinder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject internal constructor(
    private val recipeFinder: RecipeFinder,
    private val favouriteRecipesCache: FavouriteRecipesCache
): ViewModel() {

    private val recipesFlow = MutableStateFlow<List<Recipe>>(listOf())

    val resultsViewData: LiveData<List<SearchResultViewData>> = recipesFlow.map { recipes ->
        recipes.map { SearchResultViewData(it) }
    }.asLiveData()

    fun findRecipe(query: String) = viewModelScope.launch {
        val result = recipeFinder.find(query)
        if (result.isSuccessful) {
            recipesFlow.value = result.data ?: listOf()
        } else {
            //TODO - Add error handling
        }
    }

    /*fun favourite(recipeId: Int) {
        val recipe = recipesFlow.value.firstOrNull { it.id == recipeId } ?: return
        favouriteRecipesCache.insert(recipe)
    }*/
}

data class FavouritesViewData(
    val name: String,
    val imageUrl: String,
    val isFavourite: Boolean
) {
    constructor(recipe: Recipe): this(
        name = recipe.name,
        imageUrl = recipe.imageUrl,
        isFavourite = recipe.isFavourite
    )
}