package com.boitshoko.spatula.domain.models

import com.boitshoko.spatula.api.models.RecipeResponse
import com.boitshoko.spatula.db.models.RecipeStore

data class Recipe(
    val name: String,
    val imageUrl: String,
    val isFavourite: Boolean
) {
    constructor(store: RecipeStore): this(
        name = store.name,
        imageUrl = store.imageUrl,
        isFavourite = store.isFavourite
    )

    constructor(response: RecipeResponse): this(
        name = response.title,
        imageUrl = response.image,
        isFavourite = false //TODO - Figure out how to set this
    )
}