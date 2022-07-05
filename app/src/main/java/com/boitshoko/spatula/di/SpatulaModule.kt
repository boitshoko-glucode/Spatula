package com.boitshoko.spatula.di

import android.app.Application
import androidx.room.Room
import com.boitshoko.spatula.api.RecipesAPI
import com.boitshoko.spatula.db.RecipeDatabase
import com.boitshoko.spatula.repo.RecipesRepo
import com.boitshoko.spatula.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SpatulaModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRecipeApi(okHttpClient: OkHttpClient): RecipesAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RecipesAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(db: RecipeDatabase, api: RecipesAPI) : RecipesRepo {
        return  RecipesRepo(db.dao, api)
    }

    @Provides
    @Singleton
    fun provideRecipeDatabase(app: Application) : RecipeDatabase {
        return Room.databaseBuilder(
            app, RecipeDatabase::class.java, "recipe_db.db"
        ).build()
    }
}