package com.boitshoko.spatula.domain.models

import com.boitshoko.spatula.api.models.RecipeResponse
import com.boitshoko.spatula.db.models.RecipeStore

data class Recipe(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val isFavourite: Boolean
) {

    constructor(store: RecipeStore): this(
        id = store.id,
        name = store.name,
        imageUrl = store.imageUrl,
        isFavourite = store.isFavourite
    )

    constructor(response: RecipeResponse): this(
        id = response.id,
        name = response.title,
        imageUrl = response.image,
        isFavourite = false //TODO - Figure out how to set this
    )
}