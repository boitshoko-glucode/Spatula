package com.boitshoko.spatula.models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.boitshoko.spatula.repo.RecipesRepo

class RecipesViewModelProviderFactory(
    val app: Application,
    val recipesRepo: RecipesRepo
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipesViewModel(app, recipesRepo) as T
    }
    
}