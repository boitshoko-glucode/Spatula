package com.boitshoko.spatula.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.boitshoko.spatula.MainActivity
import com.boitshoko.spatula.R
import com.boitshoko.spatula.databinding.FragmentHomeBinding
import com.boitshoko.spatula.databinding.FragmentSearchRecipesBinding
import com.boitshoko.spatula.models.RecipesViewModel
import com.boitshoko.spatula.utils.Resource
import kotlinx.android.synthetic.main.fragment_search_recipes.view.*


class SearchRecipesFragment : Fragment() {
    private lateinit var binding: FragmentSearchRecipesBinding

    // private val viewModel: RecipesViewModel by viewModels()

    lateinit var viewModel: RecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    viewModel.searchRecipes(binding.etSearch.text.toString())
                    true
                }
                else -> false
            }
        }


        viewModel._searchRecipes.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {

                }

                is Resource.Error -> {
                    response.message.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    Toast.makeText(activity, "Loading", Toast.LENGTH_LONG)
                        .show()
                }
            }


        }
    }
}