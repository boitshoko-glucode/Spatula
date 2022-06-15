package com.boitshoko.spatula.di

import com.boitshoko.spatula.api.RecipesAPI
import com.boitshoko.spatula.domain.gateways.RecipesGateway
import com.boitshoko.spatula.domain.gateways.RecipesGatewayImpl
import com.boitshoko.spatula.domain.usecases.RecipeFinder
import com.boitshoko.spatula.domain.usecases.RecipeFinderImpl
import com.boitshoko.spatula.utils.Constants
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
class CoreModule {
    @Provides
    @Singleton
     fun retrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun recipesAPI(retrofit: Retrofit): RecipesAPI = retrofit.create(RecipesAPI::class.java)

    @Provides
    @Singleton
    fun recipesGateway(impl: RecipesGatewayImpl): RecipesGateway = impl

    @Provides
    @Singleton
    fun recipesFinder(iml: RecipeFinderImpl): RecipeFinder = iml
}