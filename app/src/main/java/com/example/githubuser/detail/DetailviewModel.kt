package com.example.githubuser.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.local.DbModul
import com.example.githubuser.data.model.ResponseUser
import com.example.githubuser.data.networks.ApiConfig
import com.example.githubuser.util.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailviewModel(private val db: DbModul) : ViewModel(){
    val resulDetailUser = MutableLiveData<Result>()
    val resulFollowersUser = MutableLiveData<Result>()
    val resulFollowingUser = MutableLiveData<Result>()
    val resultSuccessFavorite = MutableLiveData<Boolean>()
    val resultDeleteFavorite = MutableLiveData<Boolean>()

    private var isFavorite = false
    fun setFavorite(item: ResponseUser.Item?){
        viewModelScope.launch {
            item?.let {
                if (isFavorite){
                    db.userDao.delete(item)
                    resultDeleteFavorite.value= true
                }else{
                    db.userDao.insert(item)
                    resultSuccessFavorite.value= false
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findFavorite(id:Int, listenerFavorite:() -> Unit){
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user !=null){
              listenerFavorite
                isFavorite = true            }
        }
    }


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
    class Factory(private val db: DbModul): ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailviewModel(db) as T
        }
    }
