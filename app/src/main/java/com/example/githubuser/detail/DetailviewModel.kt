package com.example.githubuser.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.networks.ApiConfig
import com.example.githubuser.util.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailviewModel : ViewModel(){
    val resulDetailUser = MutableLiveData<Result>()
    val resulFollowersUser = MutableLiveData<Result>()
    val resulFollowingUser = MutableLiveData<Result>()

    fun getDetailUser(username : String) {
        viewModelScope.launch{
            flow {
                val response = ApiConfig
                    .apiService
                    .getDetailUser(username)

                emit(response)

            }.onStart {
                resulDetailUser.value = Result.Loading(true)
            }
                .onCompletion {
                    resulDetailUser.value = Result.Loading(false)
                }.catch {
                    Log.e("Error",it.message.toString())
                    it.printStackTrace()
                    resulDetailUser.value = Result.Error(it)
                }.collect {
                    resulDetailUser.value = Result.Success(it)
                }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .apiService
                    .getFollowersUser(username)

                emit(response)
            }.onStart {
                resulFollowersUser.value = Result.Loading(true)
            }.onCompletion {
                resulFollowersUser.value = Result.Loading(false)
            }.catch {
                it.printStackTrace()
                resulFollowersUser.value = Result.Error(it)
            }.collect {
                resulFollowersUser.value = Result.Success(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch{
            flow {
                val response = ApiConfig
                    .apiService
                    .getFollowingUser(username)

                emit(response)

            }.onStart {
                resulFollowingUser.value = Result.Loading(true)
            }
                .onCompletion {
                    resulFollowingUser.value = Result.Loading(false)
                }.catch {
                    it.printStackTrace()
                    resulFollowingUser.value = Result.Error(it)
                }.collect {
                    resulFollowingUser.value = Result.Success(it)
                }
        }
    }
}