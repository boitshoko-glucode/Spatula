package com.boitshoko.spatula.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boitshoko.spatula.databinding.SearchListItemBinding
import com.boitshoko.spatula.ui.viewmodel.SearchResultViewData
import com.bumptech.glide.Glide

class SearchAdapter(
    private var recipes: List<SearchResultViewData> = listOf(),
    private val onClick: (SearchResultViewData) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(recipes[position], onClick)
    }

    override fun getItemCount(): Int = recipes.count()

    @SuppressLint("NotifyDataSetChanged")
    fun update(recipes: List<SearchResultViewData>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val binding: SearchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: SearchResultViewData, onClick: (SearchResultViewData) -> Unit) {
            binding.mtrlListItemText.text = recipe.name
            binding.addToFavs.setOnClickListener {
                //TODO - Add/remove from favourites
                Log.d(TAG, "bind: ${recipe.name} clicked")
            }
            //TODO - Set favourite icon based on recipe.isFavourite
            binding.root.setOnClickListener {
                onClick(recipe)
            }

            Glide.with(itemView)
                .load(recipe.imageUrl)
                .into(binding.recipeIcon)
        }
    }
}