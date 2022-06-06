package com.boitshoko.spatula.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.boitshoko.spatula.R
import com.boitshoko.spatula.databinding.FragmentHomeBinding
import com.boitshoko.spatula.databinding.FragmentSearchRecipesBinding


class SearchRecipesFragment : Fragment() {
    private lateinit var binding: FragmentSearchRecipesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }
}