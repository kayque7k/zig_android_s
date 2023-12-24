package com.example.comics.repository

interface IRepository {

    suspend fun getComics() : ItemModel

}