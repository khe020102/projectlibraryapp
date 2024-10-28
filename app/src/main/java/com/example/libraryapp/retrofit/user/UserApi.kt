package com.example.libraryapp.retrofit.user

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("check/id/{studentNumber}")
    suspend fun checkStudentNumber(@Path(value = "studentNumber") studentNumber: String): Boolean

    @GET("check/nickname/{nickname}")
    suspend fun checkNickname(@Path(value = "nickname") nickname: String): Boolean

    @POST("signup")
    suspend fun signup(@Body user: User): UserResponse

    @POST("login")
    suspend fun login(@Body loginData: LoginData): UserResponse

    @GET("logout")
    fun logout(): Call<Void>
    
    @GET("user")
    fun getUserInfo(): UserResponse
}

