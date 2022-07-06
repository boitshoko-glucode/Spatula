package com.boitshoko.spatula.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.boitshoko.spatula.R
import com.boitshoko.spatula.adapters.FavoritesAdapter
import com.boitshoko.spatula.databinding.FragmentFavoritesBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.search.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentFavoritesBinding

    private val viewModel: RecipesViewModel by viewModels()

    lateinit var adapter: FavoritesAdapter

    private lateinit var rvFavoritesList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        rvFavoritesList = binding.rvFavoritesList

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpRecipesList()
    }


    private fun setUpRecipesList() {
       /* adapter = FavoritesAdapter(recipes) {
            goToRecipe(it)
        }

        rvFavoritesList.adapter = adapter*/

        viewModel.getSavedRecipes().observe(viewLifecycleOwner) { recipes ->
            Log.d(TAG, "setUpRecipesList: $recipes")
            adapter = FavoritesAdapter(recipes) {
                goToRecipe(it)
            }

            rvFavoritesList.adapter = adapter

        }
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rvFavoritesList.addItemDecoration(dividerItemDecoration)
    }



    private fun goToRecipe(recipe: Result) {
        val bundle = Bundle().apply {
            putSerializable("recipe", recipe)
        }
        Log.d(TAG, "goToRecipe: $recipe")
        findNavController().navigate(R.id.action_actionFavorites_to_recipeDetailsFragment, bundle)
    }


}