package com.boitshoko.spatula.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeStore(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val isFavourite: Boolean
)