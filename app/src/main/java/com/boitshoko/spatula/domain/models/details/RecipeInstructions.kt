package com.boitshoko.spatula.domain.models.details

import com.boitshoko.spatula.models.details.RecipeInstructionsResponse

data class RecipeInstructions(val instructions: RecipeInstructions){

    constructor(response: RecipeInstructionsResponse): this(
        instructions = response[0].steps
    )
}

