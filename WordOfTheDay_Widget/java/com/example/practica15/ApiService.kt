package com.example.practica15

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("word/")
    fun getAllWords(): Call<List<Word>>
}