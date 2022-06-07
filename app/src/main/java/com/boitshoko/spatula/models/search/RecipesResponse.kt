package com.boitshoko.spatula.models.search

data class RecipesResponse (

	val results : List<Result>,
	val offset : Int,
	val number : Int,
	val totalResults : Int
)