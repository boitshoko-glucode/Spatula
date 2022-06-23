package com.boitshoko.spatula.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.boitshoko.spatula.MainActivity
import com.boitshoko.spatula.R
import com.boitshoko.spatula.adapters.SearchAdapter
import com.boitshoko.spatula.databinding.FragmentSearchRecipesBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.models.search.Result
import com.boitshoko.spatula.utils.Resource


class SearchRecipesFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private lateinit var binding: FragmentSearchRecipesBinding

    // private val viewModel: RecipesViewModel by viewModels()

    lateinit var viewModel: RecipesViewModel

    private  var recipesList: MutableList<Result>? = null

    private lateinit var adapter: SearchAdapter
    private lateinit var rvSearchResults: RecyclerView

    private var recipeStr: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            recipeStr = it.getString("recipeStr").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchRecipesBinding.inflate(inflater, container, false)

        rvSearchResults = binding.rvSearchResults

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setQueryText()
        searchClickListener()
        gotoCamera()
        setUpObserver()
    }

    private fun gotoCamera() {
        binding.filledTextField.setEndIconOnClickListener {
            findNavController().navigate(R.id.action_actionSearch_to_cameraFragment)
        }
    }

    private fun searchClickListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    if (recipesList?.isNotEmpty() == true) recipesList!!.clear()
                    viewModel.searchRecipes(binding.etSearch.text.toString())
                    true
                }
                else -> false
            }
        }
    }

    private fun setQueryText() {
        if (recipeStr != null && recipeStr != "null") {
            binding.etSearch.setText(recipeStr)
            showKeyboard()
        }
    }

    private fun showKeyboard() {
        binding.etSearch.requestFocus()
        val inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }


    private fun setUpObserver(){
        viewModel.searchRecipes.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    showList()
                    response.data.let { recipesResponse ->
                        if (recipesResponse != null) {
                            recipesList = recipesResponse.results.toList() as MutableList<Result>
                            setUpRecipesList(recipesList!!)
                        }

                    }

                }

                is Resource.Error -> {
                    hideProgressBar()
                    showList()
                    response.message.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    hideList()
                    showProgressBar()
                }
            }

        }
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


    private fun setUpRecipesList(recipes: List<Result>) {
        adapter = SearchAdapter(recipes) {
            goToRecipe(it)
        }
        rvSearchResults.adapter = adapter
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        rvSearchResults.addItemDecoration(dividerItemDecoration)
    }



    private fun goToRecipe(recipe: Result) {
        val bundle = Bundle().apply {
            putSerializable("recipe", recipe)
        }
        Log.d(TAG, "goToRecipe: $recipe")
        findNavController().navigate(R.id.action_actionSearch_to_recipeDetailsFragment, bundle)
    }
}