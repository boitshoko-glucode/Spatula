package com.boitshoko.spatula.models

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boitshoko.spatula.MainApplication
import com.boitshoko.spatula.api.models.RecipesResponse
import com.boitshoko.spatula.db.models.RecipeStore
import com.boitshoko.spatula.models.details.RecipeInstructionsResponse
import com.boitshoko.spatula.models.search.Result
import com.boitshoko.spatula.repo.RecipesRepo
import com.boitshoko.spatula.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RecipesViewModel(
    app: Application,
    private val recipesRepo: RecipesRepo)
    : AndroidViewModel(app) {

    private val TAG = javaClass.simpleName

    val searchRecipes: MutableLiveData<Resource<RecipesResponse>> = MutableLiveData()
    //val searchRecipesResponse: LiveData<Resource<RecipesResponse>> = _searchRecipes
    var searchRecipesResponse: RecipesResponse? = null

    val recipeInstructions: MutableLiveData<Resource<RecipeInstructionsResponse>> = MutableLiveData()
    var recipeInstructionsResponse: RecipeInstructionsResponse? = null

    fun searchRecipes(searchQuery: String) = viewModelScope.launch {
        safeSearchRecipesCall(searchQuery)
    }

    fun getRecipeInstructions(id: Int) = viewModelScope.launch {
        safeGetRecipeInstructionsCall(id)
    }

    private suspend fun safeGetRecipeInstructionsCall(id: Int) {
        Log.d(TAG, "safeGetRecipeInstructionsCall: id: $id")
        recipeInstructionsResponse = null
        recipeInstructions.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = recipesRepo.getRecipeInstructions(id)
                recipeInstructions.postValue(handleRecipeInstructionsResponse(response))
            } else {
                recipeInstructions.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            Log.e(TAG, "safeGetRecipeInstructionsCall: $t")
            when (t) {
                is IOException -> recipeInstructions.postValue(Resource.Error("Network failure"))
                else -> recipeInstructions.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchRecipesCall(searchQuery: String) {
        Log.d(TAG, "safeSearchRecipesCall: searchQuery: $searchQuery")
        searchRecipesResponse = null
        searchRecipes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = recipesRepo.searchRecipes(searchQuery)
                searchRecipes.postValue(handleSearchRecipesResponse(response))
            } else {
                searchRecipes.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            Log.e(TAG, "safeSearchRecipesCall: $t")
            when (t) {
                is IOException -> searchRecipes.postValue(Resource.Error("Network failure"))
                else -> searchRecipes.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleRecipeInstructionsResponse(response: Response<RecipeInstructionsResponse>) : Resource<RecipeInstructionsResponse>{
        Log.d(TAG, "safeGetRecipesCall: response: $response")

        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                if (recipeInstructionsResponse == null){
                    Log.d(TAG, "safeGetRecipesCall: response: $resultResponse")

                    recipeInstructionsResponse = resultResponse
                }
                return Resource.Success(recipeInstructionsResponse ?: resultResponse)
            }
        }


        return Resource.Error(response.message())
    }


    private fun handleSearchRecipesResponse(response: Response<RecipesResponse>) : Resource<RecipesResponse>{

        Log.d(TAG, "safeSearchRecipesCall: response: $response")

        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                if (searchRecipesResponse == null){
                    Log.d(TAG, "safeSearchRecipesCall: response: $resultResponse")

                    searchRecipesResponse = resultResponse
                }
                return Resource.Success(searchRecipesResponse ?: resultResponse)
            }
        }


        return Resource.Error(response.message())
    }


    fun saveRecipe(recipe: Result) = viewModelScope.launch {
        recipesRepo.insertRecipe(recipe)
    }

//    fun getSavedRecipes() = recipesRepo.getSavedRecipes()
    fun getSavedRecipes() = MutableLiveData<List<Result>>()

//    fun getRandomRecipe() = recipesRepo.getRandomRecipe()
    fun getRandomRecipe() = MutableLiveData<RecipeStore>()

    fun deleteRecipe(recipe: Result) = viewModelScope.launch {
        recipesRepo.deleteRecipe(recipe)
    }


    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MainApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false
    }
}