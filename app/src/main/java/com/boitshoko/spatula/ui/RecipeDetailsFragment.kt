package com.boitshoko.spatula.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boitshoko.spatula.MainActivity
import com.boitshoko.spatula.R
import com.boitshoko.spatula.adapters.FavoritesAdapter
import com.boitshoko.spatula.databinding.FragmentHomeBinding
import com.boitshoko.spatula.databinding.FragmentRecipeDetailsBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.search.Result
import com.google.android.material.snackbar.Snackbar


class RecipeDetailsFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentRecipeDetailsBinding

    lateinit var viewModel: RecipesViewModel

    lateinit var adapter: FavoritesAdapter


    private lateinit var resultRecipe: Result

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        resultRecipe = arguments?.getSerializable("recipe") as Result
        Log.d(TAG, "onViewCreated: $resultRecipe")

        binding.fab.setOnClickListener {
            viewModel.saveRecipe(resultRecipe)
            Snackbar.make(view, "Recipe saved", Snackbar.LENGTH_SHORT).show()
        }
    }
}