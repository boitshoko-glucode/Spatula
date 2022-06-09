package com.boitshoko.spatula.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boitshoko.spatula.models.search.Result

@Database(
    entities = [Result::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RecipeDatabase: RoomDatabase() {

    abstract fun getRecipeDao(): RecipeDao
    
    companion object {
        @Volatile
        private var instance: RecipeDatabase? = null
        private val LOCK = Any()
        
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipe_db.db"
            ).fallbackToDestructiveMigration().build()
    }
}