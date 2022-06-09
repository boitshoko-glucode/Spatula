package com.boitshoko.spatula.models

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.boitshoko.spatula.MainApplication
import com.boitshoko.spatula.models.search.RecipesResponse
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

    val _searchRecipes: MutableLiveData<Resource<RecipesResponse>> = MutableLiveData()
    //val searchRecipesResponse: LiveData<Resource<RecipesResponse>> = _searchRecipes
    var searchRecipesResponse: RecipesResponse? = null

    fun searchRecipes(searchQuery: String) = viewModelScope.launch {
        safeSearchRecipesCall(searchQuery)
    }

    private suspend fun safeSearchRecipesCall(searchQuery: String) {
        Log.d(TAG, "safeSearchRecipesCall: searchQuery: $searchQuery")
        searchRecipesResponse = null
        _searchRecipes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = recipesRepo.searchRecipes(searchQuery)
                _searchRecipes.postValue(handleSearchRecipesResponse(response))
            } else {
                _searchRecipes.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            Log.e(TAG, "safeSearchRecipesCall: $t")
            when (t) {
                is IOException -> _searchRecipes.postValue(Resource.Error("Network failure"))
                else -> _searchRecipes.postValue(Resource.Error("Conversion Error"))
            }
        }
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

    fun getSavedRecipes() = recipesRepo.getSavedRecipes()

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