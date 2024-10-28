package com.example.libraryapp.retrofit.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("studentID") var studentID: String,
    @SerializedName("password") var password: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("phone_number") var phoneNumber: String,
)
data class UserResponse(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("studentID") var studentID: String,
    @SerializedName("password") var password: String,
    @SerializedName("nickname") var nickname: String,
    @SerializedName("phone_number") var phoneNumber: String,
    @SerializedName("enabled") var enabled: Boolean,
    @SerializedName("authorities") var authorities: List<Author>,
    @SerializedName("username") var username: String,
    @SerializedName("accountNonExpired") var accountNonExpired: Boolean,
    @SerializedName("accountNonLocked") var accountNonLocked: Boolean,
    @SerializedName("credentialsNonExpired") var credentialsNonExpired: Boolean,
)

data class Author(
    var authority: String = ""
)

data class LoginData(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
