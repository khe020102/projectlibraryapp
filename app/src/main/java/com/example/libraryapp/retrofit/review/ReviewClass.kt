package com.example.libraryapp.retrofit.review

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("title") var title: String,
    @SerializedName("content") var content: String,
    @SerializedName("isbnNo") var isbnNo: String,
    @SerializedName("score") var score: String
)
data class ReviewResponse(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("content") var content: String,
    @SerializedName("isbnNo") var isbnNo: String,
    @SerializedName("studentnumber") var studentNumber: String,
    @SerializedName("score") var score: String
)
data class ModiReview(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("content") var content: String,
    @SerializedName("isbnNo") var isbnNo: String,
    @SerializedName("studentnumber") var studentNumber: String,
    @SerializedName("score") var score: String
)