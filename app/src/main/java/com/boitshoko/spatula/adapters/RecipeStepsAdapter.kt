package com.boitshoko.spatula.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boitshoko.spatula.databinding.FavoritesListItemBinding
import com.boitshoko.spatula.databinding.RecipeListItemBinding
import com.boitshoko.spatula.databinding.SearchListItemBinding
import com.boitshoko.spatula.models.search.Result
import com.bumptech.glide.Glide

class RecipeStepsAdapter(
    private var recipes: List<String>
) : RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder>() {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeStepsViewHolder {
        return RecipeStepsViewHolder(RecipeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecipeStepsViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.count()


    inner class RecipeStepsViewHolder(private val binding: RecipeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: String) {
            binding.desc.text = recipe

        }
    }
}