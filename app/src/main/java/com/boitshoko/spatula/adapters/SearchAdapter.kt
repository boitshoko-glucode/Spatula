package com.boitshoko.spatula.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boitshoko.spatula.databinding.SearchListItemBinding
import com.boitshoko.spatula.models.search.Result
import com.bumptech.glide.Glide

class SearchAdapter(
    private var recipes: List<Result>,
    private val onClick: (Result) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(recipes[position], onClick)
    }

    override fun getItemCount(): Int = recipes.count()


    inner class SearchViewHolder(private val binding: SearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Result, onClick: (Result) -> Unit) {
            binding.mtrlListItemText.text = recipe.title
            val media = recipe.image
            Glide.with(itemView)
                .load(media)
                .into(binding.recipeIcon)
            binding.addToFavs.setOnClickListener {
                Log.d(TAG, "bind: ${recipe.title} clicked")
            }
            //binding.mtrlListItemSecondaryText.text = recipe.nutrition
            //binding.mtrlListItemTertiaryText.text = engineer.role
            binding.root.setOnClickListener {
                onClick(recipe)
            }

        }
    }
}