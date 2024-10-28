package com.example.libraryapp.retrofit.review

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReviewAPI {
    @GET("reviews")
    fun getMyReviews(): Call<List<ReviewResponse>>

    @GET("reviews/{isbnNo}")
    fun getBookReviews(@Path(value = "isbnNo") isbnNo : String): Call<List<ReviewResponse>>
    
    @POST("reviews")
    fun postReview(@Body review: Review): Call<ReviewResponse>

    @DELETE("reviews/{isbnNo}")
    fun deleteReview(@Path(value = "isbnNo") isbnNo : String): Call<Void>

    @PUT("reviews/{id}")
    fun modifyReviews(@Path(value = "id") id : Int, @Body modiReview: ModiReview): Call<ReviewResponse>
}
