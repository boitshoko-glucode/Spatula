package com.boitshoko.spatula.ui

import android.animation.LayoutTransition
import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.boitshoko.spatula.MainActivity
import com.boitshoko.spatula.R
import com.boitshoko.spatula.adapters.RecipeStepsAdapter
import com.boitshoko.spatula.adapters.SearchAdapter
import com.boitshoko.spatula.databinding.FragmentRecipeDetailsBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.details.Equipment
import com.boitshoko.spatula.models.details.Ingredient
import com.boitshoko.spatula.models.details.RecipeInstructionsResponseItem
import com.boitshoko.spatula.models.search.Result
import com.boitshoko.spatula.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar


class RecipeDetailsFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentRecipeDetailsBinding

    lateinit var viewModel: RecipesViewModel


    private lateinit var resultRecipe: Result
    private lateinit var stepList: MutableList<String>
    private lateinit var ingredientsList: MutableList<Ingredient>
    private lateinit var equipmentList: MutableList<Equipment>

    private lateinit var recipeStepsAdapter: RecipeStepsAdapter
    private lateinit var rvRecipeSteps: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        //rvRecipeSteps = binding.recipeLayout.recipeDetailsText
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
        viewModel.getRecipeInstructions(resultRecipe.id)

        binding.recipeTitle.text = resultRecipe.title

        val media = resultRecipe.image
        Glide.with(this)
            .load(media)
            .into(binding.media)

        binding.fab.setOnClickListener {
            viewModel.saveRecipe(resultRecipe)
            Snackbar.make(view, "Recipe saved", Snackbar.LENGTH_SHORT).show()
        }

        binding.recipeCard.setOnClickListener {
            expand(binding.recipeDetailsText, binding.recipeView)
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
        //setUpRecipeSteps()

        binding.recipeDetailsText.text = stepListToText()
    }

    private fun stepListToText() : String {
        var stepsStr = ""
        for (i in stepList.indices) {
            stepsStr += "${i+1} ${stepList[i]}\n\n"
        }


        return stepsStr
    }

    private fun ingredientsListToText() : String {
        var ingredientsStr = ""
        for (ingredient in ingredientsList) {
            ingredientsStr += "${ingredient.name}, "
        }
        return ingredientsStr
    }

    private fun equipmentListToText() : String {
        var equipmentStr = ""
        for (equipment in equipmentList) {
            equipmentStr += "${equipment.name}, "
        }
        return equipmentStr
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

    fun setUpRecipeSteps() {
        recipeStepsAdapter = RecipeStepsAdapter(stepList)
        rvRecipeSteps.adapter = recipeStepsAdapter
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rvRecipeSteps.addItemDecoration(dividerItemDecoration)
    }

    private fun showView(view: View) {
        view.visibility = View.VISIBLE
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