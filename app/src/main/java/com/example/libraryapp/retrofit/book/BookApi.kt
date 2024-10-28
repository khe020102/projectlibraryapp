package com.example.libraryapp.retrofit.book


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookApi {
    @GET("books")
    fun getBooks(): List<Book>
    
    //특정 책 조회 api
    @GET("books/{isbnNo}")
    fun getBook(@Path(value = "isbnNo") isbnNo: String): Call<Book>
    
    //검색 api
    @GET("search/{key_word}")
    fun searchBook(@Path(value = "key_word") keyWord: String): Call<List<Book>>
    
    //인기도서 api
    @GET("popularbooks")
    fun getPopularBooks(): Call<List<Book>>
    
    //키워드  등록 api
    @POST("keywords")
    suspend fun postKeywords(@Body keywords: Keyword): KeywordResponse
    
    //추천도서 조회 api
    @GET("recommend")
    fun getRecommendBook(): Call<List<Book>>
    
    //즐겨찾기 등록, 삭제, 조회 api
    @POST("favoritebook")
    fun postFavorite(@Body favorite: Favorite): Call<FavoriteResponse>
    
    @DELETE("favoritebook/{isbnNo}")
    fun deleteFavorite(@Path(value = "isbnNo") isbnNo: String): Call<Void>
    
    @GET("favoritebook")
    fun getMyFavoriteBooks(): Call<List<Book>>
    
    @GET("favoritebook/{isbnNo}")
    fun isBookFavorite(@Path(value = "isbnNo") isbnNo: String): Call<Boolean>
    
    //책 등록,삭제,수정
    @POST("books")
    fun postBooks(@Body book: Book): Book
    
    @DELETE("books/{isbnNo}")
    fun deleteBooks(@Path(value = "isbnNo") isbnNo: String): Void

    @PUT("books/{isbnNo}")
    fun modifyBooks(@Path(value = "isbnNo") isbnNo: String, @Body book: Book): Book
}