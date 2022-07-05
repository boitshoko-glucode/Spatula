package com.boitshoko.spatula.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boitshoko.spatula.models.search.Result

@Database(
    entities = [Result::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase: RoomDatabase() {

    abstract val dao: RecipeDao

}