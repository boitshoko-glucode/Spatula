package com.boitshoko.spatula

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.boitshoko.spatula.databinding.ActivityMainBinding
import com.boitshoko.spatula.db.RecipeDatabase
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.RecipesViewModelProviderFactory
import com.boitshoko.spatula.repo.RecipesRepo

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: RecipesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val newsRepo = RecipesRepo(RecipeDatabase(this))
        val viewModelProviderFactory = RecipesViewModelProviderFactory(application, newsRepo)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(RecipesViewModel::class.java)

        /*val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)*/
      //  binding.mainNavBnv.setupWithNavController(navController)
        setupBottomNav()
    }


    private fun setupBottomNav() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.actionFavorites -> showBottomNav()
                R.id.actionHome -> showBottomNav()
                R.id.actionSearch -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE

    }
}