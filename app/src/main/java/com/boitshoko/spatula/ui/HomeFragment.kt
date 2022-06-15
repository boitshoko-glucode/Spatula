package com.boitshoko.spatula.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.boitshoko.spatula.MainActivity
import com.boitshoko.spatula.R
import com.boitshoko.spatula.databinding.FragmentHomeBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.details.Equipment
import com.boitshoko.spatula.models.details.Ingredient
import com.boitshoko.spatula.models.details.RecipeInstructionsResponseItem
import com.boitshoko.spatula.utils.Resource
import com.bumptech.glide.Glide

class HomeFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentHomeBinding

    lateinit var viewModel: RecipesViewModel

    private lateinit var stepList: MutableList<String>
    private lateinit var ingredientsList: MutableList<Ingredient>
    private lateinit var equipmentList: MutableList<Equipment>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        viewModel = (activity as MainActivity).viewModel

        stepList = mutableListOf()
        ingredientsList = mutableListOf()
        equipmentList = mutableListOf()

        getRandomRecipe()
        setUpObserver()
    }


    private fun getRandomRecipe() {

        viewModel.getRandomRecipe().observe(viewLifecycleOwner) { recipe ->
            Log.d(TAG, "setUpRandomRecipe: $recipe")
            viewModel.getRecipeInstructions(recipe.id)
            setImage(recipe.imageUrl)
            setTitle(recipe.name)

        }
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

                            showViews()

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
        //setUpRecipeSteps()

        binding.recipeDetailsText.text = stepListToText()
    }

    private fun stepListToText() : String {
        var stepsStr = ""
        for (i in stepList.indices) {
            stepsStr += "${i+1}. ${stepList[i]}\n\n"
        }


        return stepsStr.dropLast(2)
    }

    private fun ingredientsListToText() : String {
        var ingredientsStr = ""
        for (ingredient in ingredientsList) {
            ingredientsStr += "${ingredient.name}, "
        }
        return ingredientsStr.dropLast(2)
    }

    private fun equipmentListToText() : String {
        var equipmentStr = ""
        for (equipment in equipmentList) {
            equipmentStr += "${equipment.name}, "
        }
        return equipmentStr.dropLast(2)
    }

    private fun getIngredients(recipeItem: RecipeInstructionsResponseItem) {
        for (step in recipeItem.steps) {
            for (ing in step.ingredients) ingredientsList.add(ing)
        }

        binding.recipeIngredients.text = getString(R.string.ingredients, ingredientsListToText())
    }

    private fun getEquipment(recipeItem: RecipeInstructionsResponseItem) {
        for (step in recipeItem.steps) {
            for (eq in step.equipment) equipmentList.add(eq)
        }

       binding.recipeEquipment.text = getString(R.string.equipment, equipmentListToText())
    }

    private fun setImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(binding.recipeImage)
    }

    private fun setTitle(title: String){
        binding.recipeTitle.text = title
    }

    private fun showViews() {
        binding.card.visibility = View.VISIBLE
        binding.recipeView.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


}

