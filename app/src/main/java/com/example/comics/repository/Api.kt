package com.example.comics.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


private const val ENDPOINT_COMICS = "comics"
private const val TS_QUERY = "ts"
private const val APIKEY_QUERY = "apikey"
private const val HASH_QUERY = "hash"

interface Api {

    @GET(ENDPOINT_COMICS)
    fun getComics(
        @Query(TS_QUERY) ts: String,
        @Query(APIKEY_QUERY) apikey: String,
        @Query(HASH_QUERY) hash: String
    ) : Call<ItemModel>
}