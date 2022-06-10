package com.boitshoko.spatula.models.details

data class RecipeInstructionsResponseItem(
    val name: String,
    val steps: List<Step>
)