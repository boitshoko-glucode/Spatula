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
import com.boitshoko.spatula.adapters.SearchAdapter
import com.boitshoko.spatula.databinding.FragmentSearchRecipesBinding
import com.boitshoko.spatula.extensions.setOnSearchListener
import com.boitshoko.spatula.models.search.Result
import com.boitshoko.spatula.ui.viewmodel.SearchResultViewData
import com.boitshoko.spatula.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipesFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentSearchRecipesBinding

     private val viewModel: SearchViewModel by viewModels()

    private  var recipesList: MutableList<Result>? = null
    private lateinit var rvSearchResults: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchRecipesBinding.inflate(inflater, container, false)

        rvSearchResults = binding.rvSearchResults



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureList()

        binding.etSearch.setOnSearchListener {
            if (recipesList?.isNotEmpty() == true) recipesList!!.clear()
            viewModel.findRecipe(binding.etSearch.text.toString())
        }

        setUpObserver()
    }

    private fun configureList() = with(binding.rvSearchResults) {
        adapter = SearchAdapter {
            goToRecipe(it)
        }
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    private fun setUpObserver() {
        viewModel.resultsViewData.observe(viewLifecycleOwner) { viewData ->
            (binding.rvSearchResults.adapter as? SearchAdapter)?.update(viewData)
        }
//        viewModel.searchRecipes.observe(viewLifecycleOwner) { response ->
//            when (response) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    showList()
//                    response.data.let { recipesResponse ->
//                        if (recipesResponse != null) {
//                            recipesList = recipesResponse.results.toList() as MutableList<Result>
//                            setUpRecipesList(recipesList!!)
//                        }
//
//                    }
//
//                }
//
//                is Resource.Error -> {
//                    hideProgressBar()
//                    showList()
//                    response.message.let { message ->
//                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
//                            .show()
//                    }
//                }
//
//                is Resource.Loading -> {
//                    hideList()
//                    showProgressBar()
//                }
//            }
//
//        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        //isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
       // isLoading = true
    }

    private fun hideList() {
        binding.rvSearchResults.visibility = View.INVISIBLE
        //isLoading = false
    }

    private fun showList() {
        binding.rvSearchResults.visibility = View.VISIBLE
        // isLoading = true
    }



    private fun goToRecipe(recipe: SearchResultViewData) {
        val bundle = Bundle().apply {
//            putSerializable("recipe", recipe)
        }
        Log.d(TAG, "goToRecipe: $recipe")
        findNavController().navigate(R.id.action_actionSearch_to_recipeDetailsFragment, bundle)
    }
}