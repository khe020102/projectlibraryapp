package com.example.libraryapp.retrofit.book
import retrofit2.Call
import retrofit2.http.GET

interface AladinApi {
    @GET("bestseller")
    fun bestseller(): Call<List<Aladin>>

    @GET("newbook")
    fun newbook(): Call<List<Aladin>>
}
