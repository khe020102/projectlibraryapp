package com.example.libraryapp.retrofit.book

import com.google.gson.annotations.SerializedName

data class Aladin(
    @SerializedName("cover") var cover: String,
    @SerializedName("link") var link: String,
    @SerializedName("title") var title: String,
)
