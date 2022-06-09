package com.boitshoko.spatula.models.search

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
	tableName = "recipes"
)

data class Result (
	@PrimaryKey val id : Int,
	val title : String,
	val image : String,
	val imageType : String,
	//val nutrition : Nutrition
): Serializable