package com.example.libraryapp.retrofit

import com.example.libraryapp.retrofit.book.AladinApi
import com.example.libraryapp.retrofit.book.BookApi
import com.example.libraryapp.retrofit.review.ReviewAPI
import com.example.libraryapp.retrofit.user.UserApi
import okhttp3.OkHttpClient
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClientInstance {
    private const val BASE_URL = "http://52.78.146.166:8080/api/"

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(CustomCookieJar()).build()
    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(
            okHttpClient)
            .build()

    val userApi: UserApi = retrofit.create(UserApi::class.java)

    var bookApi: BookApi = retrofit.create(BookApi::class.java)

    val reviewApi: ReviewAPI = retrofit.create(ReviewAPI::class.java)

    var aladinApi: AladinApi = retrofit.create(AladinApi::class.java)
}


class CustomCookieJar : CookieJar {
    private val cookieStore: MutableMap<String, MutableList<Cookie>> = mutableMapOf()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies.toMutableList()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: mutableListOf()
    }
}
