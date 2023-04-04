package com.example.githubuser.data.networks


import com.example.githubuser.BuildConfig
import com.example.githubuser.data.model.ResponseDetailUser
import com.example.githubuser.data.model.ResponseUser
import retrofit2.http.*


interface ApiService {
    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUser(
        @Header("Authorization")
        authorization: String = BuildConfig.TOKEN
    ): MutableList<ResponseUser.Item>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getDetailUser(@Path ("username")username: String,
     @Header("Authorization")
     authorization: String = BuildConfig.TOKEN
    ): ResponseDetailUser

    @JvmSuppressWildcards
    @GET("/users/{username}/followers")
    suspend fun getFollowersUser(@Path ("username")username: String,
    @Header("Authorization")
    authorization: String = BuildConfig.TOKEN
    ): MutableList<ResponseUser.Item>

    @JvmSuppressWildcards
    @GET("/users/{username}/following")
    suspend fun getFollowingUser(@Path ("username")username: String,
    @Header("Authorization")
    authorization: String = BuildConfig.TOKEN
    ): MutableList<ResponseUser.Item>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun searchUser ( @QueryMap params: Map<String, Any>,
    @Header("Authorization")
    authorization: String = BuildConfig.TOKEN
    ): ResponseUser
}
