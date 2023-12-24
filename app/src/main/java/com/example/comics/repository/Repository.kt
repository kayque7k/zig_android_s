package com.example.comics.repository

import com.example.comics.BuildConfig
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class Repository : IRepository {

    override suspend fun getComics() = Retrofit.Builder()
        .baseUrl(BuildConfig.GATEWAY)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(Api::class.java).getComics(
            apikey = BuildConfig.API_KEY,
            ts = BuildConfig.TS,
            hash = BuildConfig.HASH
        ).await()

}