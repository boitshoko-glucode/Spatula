package com.boitshoko.spatula.ui

import android.animation.LayoutTransition
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import com.boitshoko.spatula.MainActivity
import com.boitshoko.spatula.databinding.FragmentRecipeDetailsBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.details.Equipment
import com.boitshoko.spatula.models.details.Ingredient
import com.boitshoko.spatula.models.details.RecipeInstructionsResponseItem
import com.boitshoko.spatula.models.search.Result
import com.boitshoko.spatula.utils.Resource
import com.google.android.material.snackbar.Snackbar


class RecipeDetailsFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentRecipeDetailsBinding

    lateinit var viewModel: RecipesViewModel


    private lateinit var resultRecipe: Result
    private lateinit var stepList: MutableList<String>
    private lateinit var ingredientsList: MutableList<Ingredient>
    private lateinit var equipmentList: MutableList<Equipment>

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
        stepList = mutableListOf()
        ingredientsList = mutableListOf()
        equipmentList = mutableListOf()
        Log.d(TAG, "onViewCreated: $resultRecipe")
       // viewModel.getRecipeInstructions(resultRecipe.id)

        binding.fab.setOnClickListener {
            viewModel.saveRecipe(resultRecipe)
            Snackbar.make(view, "Recipe saved", Snackbar.LENGTH_SHORT).show()
        }

        binding.recipeLayout.recipeCard.setOnClickListener {
            expand(binding.recipeLayout.recipeDetailsText, binding.recipeLayout.recipeLinearLayout)
        }

        setUpObserver()
    }

    private fun setUpObserver() {

        viewModel.recipeInstructions.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { recipeResponse ->
                        if (recipeResponse != null) {
                            Log.d(TAG, "setUpObserver: $recipeResponse")
                            getInstructionSteps(recipeResponse[0])
                            getIngredients(recipeResponse[0])
                            getEquipment(recipeResponse[0])
                            Log.d(TAG, "setUpObserver: $stepList")
                            Log.d(TAG, "setUpObserver: $ingredientsList")
                            Log.d(TAG, "setUpObserver: $equipmentList")
                        }

                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        }

    }

    private fun getInstructionSteps(recipeItem: RecipeInstructionsResponseItem) {
        for (step in recipeItem.steps) stepList.add(step.step)
    }

    private fun getIngredients(recipeItem: RecipeInstructionsResponseItem) {
        for (step in recipeItem.steps) {
            for (ing in step.ingredients) ingredientsList.add(ing)
        }
    }

    private fun getEquipment(recipeItem: RecipeInstructionsResponseItem) {
        for (step in recipeItem.steps) {
            for (eq in step.equipment) equipmentList.add(eq)
        }
    }

    private fun showView() {
        TODO("Not yet implemented")
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    fun expand(view: View, viewGroup: ViewGroup) {
        viewGroup.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        val v = if (view.visibility == View.GONE) View.VISIBLE else View.GONE

        TransitionManager.beginDelayedTransition(viewGroup, AutoTransition())
        view.visibility = v
    }

}