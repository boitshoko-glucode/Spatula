package com.boitshoko.spatula.db

import androidx.room.TypeConverter
import com.boitshoko.spatula.models.search.Nutrients
import com.boitshoko.spatula.models.search.Nutrition


private const val SEPARATOR = ","

class Converters {

    @TypeConverter
    fun fromNutrition(nutrition: Nutrition): String {

        return nutrition.toString()
    }

    //todo: Make toNutrition return MutableList<Nutrients>
    @TypeConverter
    fun toNutrition(name: String): MutableList<String> {
        return name.split(SEPARATOR).toMutableList()
    }
}