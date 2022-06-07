package com.boitshoko.spatula.models.search


data class Result (

	val id : Int,
	val title : String,
	val image : String,
	val imageType : String,
	val nutrition : Nutrition
)